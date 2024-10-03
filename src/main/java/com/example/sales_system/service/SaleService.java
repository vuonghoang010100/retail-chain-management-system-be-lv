package com.example.sales_system.service;

import com.example.sales_system.dto.request.OrderCreateRequest;
import com.example.sales_system.dto.response.OrderResponse;
import com.example.sales_system.entity.tenant.*;
import com.example.sales_system.enums.*;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.OrderMapper;
import com.example.sales_system.repository.tenant.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SaleService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    OrderUsePromoteRepository orderUsePromoteRepository;
    InvoiceRepository invoiceRepository;
    PromoteRepository promoteRepository;
    BatchRepository batchRepository;

    EmployeeService employeeService;
    StoreService storeService;
    CustomerService customerService;
    BatchService batchService;

    OrderMapper orderMapper;
    private final PromoteService promoteService;


    public OrderResponse createOrder(OrderCreateRequest request) {
        // 1. Save info
        Order order = orderMapper.toOrder(request);

        if (order.getStatus().equals(OrderStatus.CANCEL))
            throw new AppException(AppStatusCode.INVALID_ORDER_CREATION_STATUS);

        if (order.getTaxPercentage() == null)
            order.setTaxPercentage(0L);

        // employee
        var employee = employeeService.getEmployeeById(request.getEmployeeId());
        if (employee.getStatus().equals(EmployeeStatus.INACTIVE))
            throw new AppException(AppStatusCode.ORDER_ON_EMPLOYEE_MUST_BE_ACTIVE);
        order.setEmployee(employee);

        // store
        var store = storeService.getStoreById(request.getStoreId());
        if (store.getStatus().equals(StoreStatus.INACTIVE))
            throw new AppException(AppStatusCode.ORDER_ON_STORE_MUST_BE_ACTIVE);
        order.setStore(store);

        // customer
        if (request.getCustomerId() != null) {
            var customer = customerService.getCustomerById(request.getCustomerId());
            if (Objects.nonNull(customer)) {
                order.setCustomer(customer);
            }
        }

        // details
        List<OrderDetail> details = request.getDetails().stream()
                .map(detailRequest -> {
                    var batch = batchService.getBatchById(detailRequest.getBatchId());
                    var product = batch.getProduct();

                    return OrderDetail.builder()
                            .quantity(detailRequest.getQuantity())
                            .price(product.getPrice())
                            .subTotal(detailRequest.getQuantity() * product.getPrice())
                            .done(false)
                            .product(product)
                            .store(store)
                            .batch(batch)
                            .build();
                }).toList();

        // promotes
        var promotes = promoteRepository.findAllById(request.getPromoteIds());
        promotes.forEach(promote -> {
            if (!promoteService.isActive(promote))
                throw new AppException(AppStatusCode.PROMOTE_IS_INACTIVE);
        });

        // save
        order = orderRepository.save(order);
        Order fixedIdOrder = order;

        // save promotes
        Set<OrderUsePromote> usePromotes = new HashSet<>();
        promotes.forEach(promote -> {
            var usePromote = OrderUsePromote.builder()
                    .order(fixedIdOrder)
                    .promote(promote)
                    .discount(0L)
                    .build();
            orderUsePromoteRepository.save(usePromote);
            usePromotes.add(usePromote);
        });
        order.setUsePromotes(usePromotes);

        // save details
        details.forEach(detail -> {
            detail.setOrder(fixedIdOrder);
            orderDetailRepository.save(detail);
        });

        order.setDetails(new HashSet<>(details));

        // 2. Calculate
        // subtotal
        Long subTotal = order.getDetails().stream().reduce(0L, (acc, ele) -> acc + ele.getSubTotal(), Long::sum);
        order.setSubTotal(subTotal);

        // apply promote
        AtomicReference<Long> discount = new AtomicReference<>(0L);
        Order finalOrder = order;
        order.getUsePromotes().stream()
                .filter(usePromote -> usePromote.getPromote().getType().equals(PromoteType.DISCOUNTPRODUCT))
                        .forEach(usePromote -> {
                            var promote = usePromote.getPromote();
                            var product = promote.getProduct();
                            var quantity = finalOrder.getDetails().stream().filter(orderDetail -> orderDetail.getProduct().equals(product))
                                    .reduce(0L, (acc, ele) -> acc + ele.getQuantity(), Long::sum);
                            long subDiscount = quantity * (product.getPrice() - promote.getDiscountPrice());
                            usePromote.setDiscount(subDiscount);
                            orderUsePromoteRepository.save(usePromote);
                            discount.set(discount.get() + subDiscount);
                            promoteService.usePromote(promote);
                        });

        Long quantity = order.getDetails().stream()
                        .reduce(0L, (acc, ele) -> acc + ele.getQuantity(), Long::sum);
        order.getUsePromotes().stream()
                .filter(usePromote -> !usePromote.getPromote().getType().equals(PromoteType.DISCOUNTPRODUCT))
                        .forEach(usePromote -> {
                            long subDiscount = 0L;
                            var promote = usePromote.getPromote();

                            // check min required
                            if (promote.getMinQuantityRequired() != null && promote.getMinQuantityRequired() > quantity)
                                throw new AppException(AppStatusCode.PROMOTE_MIN_QUANTITY_REQUIRED_DOES_NOT_MATCHED);

                            if (promote.getMinAmountRequired() != null && promote.getMinAmountRequired() > subTotal)
                                throw new AppException(AppStatusCode.PROMOTE_MIN_AMOUNT_REQUIRED_DOES_NOT_MATCHED);

                            if (promote.getType().equals(PromoteType.PERCENTAGE)) {
                                subDiscount = (subTotal / 100L) * promote.getPercentage();
                                if (subDiscount > promote.getMaxDiscount())
                                    subDiscount = promote.getMaxDiscount();
                            }
                            else {
                                subDiscount = promote.getAmount();
                            }
                            usePromote.setDiscount(subDiscount);
                            orderUsePromoteRepository.save(usePromote);
                            discount.set(discount.get() + subDiscount);
                            promoteService.usePromote(promote);
                        });

        order.setDiscount(discount.get());

        // tax
        long preTax = subTotal - discount.get();
        order.setPreTax(preTax);

        long tax = (preTax / 100L) * order.getTaxPercentage();
        order.setTax(tax);

        long total = preTax + tax;
        order.setTotal(total);

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            order.getDetails().forEach(orderDetail -> {
                orderDetail.setDone(true);
                orderDetail.setSaleDate(LocalDate.now());
                var batch = orderDetail.getBatch();
                batch.setQuantity(batch.getQuantity() - orderDetail.getQuantity());
                if (batch.getQuantity() < 0)
                    throw new AppException(AppStatusCode.INVALID_PRODUCT_QUANTITY);

                batch = batchRepository.save(batch);
                orderDetail.setBatch(batch);
                orderDetailRepository.save(orderDetail);
            });
        }

        // create Invoice
        Invoice invoice = Invoice.builder()
                .total(order.getTotal())
                .order(order)
                .paymentStatus(order.getStatus().equals(OrderStatus.COMPLETE) ? PaymentStatus.PAID : PaymentStatus.UNPAID)
                .employee(order.getEmployee())
                .store(order.getStore())
                .customer(order.getCustomer())
                .build();
        invoiceRepository.save(invoice);
        order.setInvoice(invoice);

        // return
        order = orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }
}
