package com.example.sales_system.service;

import com.example.sales_system.dto.request.BillCreateRequest;
import com.example.sales_system.dto.request.BillUpdateRequest;
import com.example.sales_system.dto.response.BillResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Bill;
import com.example.sales_system.enums.EmployeeStatus;
import com.example.sales_system.enums.PaymentStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.BillMapper;
import com.example.sales_system.repository.tenant.BillRepository;
import com.example.sales_system.repository.tenant.PurchaseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BillService {
    BillRepository billRepository;
    PurchaseRepository purchaseRepository;

    VendorService vendorService;
    EmployeeService employeeService;

    BillMapper billMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<BillResponse> getAllBills(Specification<Bill> specification, Pageable pageable) {
        Page<Bill> page = billRepository.findAll(specification, pageable);

        return ListResponse.<BillResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(billMapper::toBillResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public BillResponse getBill(Long id) {
        return billMapper.toBillResponse(getBillById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public BillResponse createBill(BillCreateRequest request) {
        Bill bill = billMapper.toBill(request);

        // init payment status
        if (Objects.isNull(bill.getPaymentStatus())) {
            bill.setPaymentStatus(PaymentStatus.UNPAID);
        }

        // vendor
        var vendor = vendorService.getVendorById(request.getVendorId());
        bill.setVendor(vendor);

        // employee
        var employee = employeeService.getEmployeeById(request.getEmployeeId());
        if (employee.getStatus().equals(EmployeeStatus.INACTIVE))
            throw new AppException(AppStatusCode.BILL_EMPLOYEE_MUST_BE_ACTIVE);
        bill.setEmployee(employee);

        // purchases
        var purchases = purchaseRepository.findAllById(request.getPurchaseIds());

        if (purchases.isEmpty()) {
            throw new AppException(AppStatusCode.PURCHASE_NOT_FOUND);
        }

        purchases.forEach(purchase -> {
            if (purchase.getPaymentStatus().equals(PaymentStatus.PAID)) {
                throw new AppException(AppStatusCode.BILL_PURCHASE_MUST_BE_UNPAID);
            }
        });

        bill.setPurchases(new HashSet<>(purchases));

        // Total
        Long total = bill.getPurchases().stream().reduce(0L, (acc, ele) -> acc + ele.getTotal(), Long::sum);
        bill.setTotal(total);

        bill = billRepository.save(bill);

        // save purchase's bill
        Bill finalBill = bill;
        bill.getPurchases().forEach(purchase -> {
            purchase.setBill(finalBill);
            purchaseRepository.save(purchase);
        });

        // check paid
        if (bill.getPaymentStatus().equals(PaymentStatus.PAID)) {
            bill = updatePaid(bill);
        }

        return billMapper.toBillResponse(bill);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public BillResponse updateBill(Long id, BillUpdateRequest request) {
        Bill bill = getBillById(id);

        bill.setNote(request.getNote());

        if (bill.getPaymentStatus().equals(request.getPaymentStatus())) {
            bill = billRepository.save(bill);
            return billMapper.toBillResponse(bill);
        }

        if (bill.getPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new AppException(AppStatusCode.BILL_PAYMENT_STATUS_CAN_NOT_REVERT);
        }

        bill.setPaymentStatus(PaymentStatus.PAID);
        bill = updatePaid(bill);
        bill = billRepository.save(bill);
        return billMapper.toBillResponse(bill);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public void deleteBill(Long id) {
        var bill = getBillById(id);
        if (bill.getPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new AppException(AppStatusCode.UNABLE_TO_DELETE_PAID_BILL);
        }
        billRepository.deleteById(id);
    }

    // ----- Helper functions -----

    @Transactional(transactionManager = "tenantTransactionManager")
    protected Bill updatePaid(Bill bill) {
        bill.getPurchases().forEach(purchase -> {
            purchase.setPaymentStatus(PaymentStatus.PAID);
            purchaseRepository.save(purchase);
        });
        return bill;
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.BILL_NOT_FOUND));
    }


}
