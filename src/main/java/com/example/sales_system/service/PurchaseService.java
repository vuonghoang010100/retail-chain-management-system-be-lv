package com.example.sales_system.service;

import com.example.sales_system.dto.request.PurchaseCreateRequest;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.PurchaseResponse;
import com.example.sales_system.entity.tenant.Purchase;
import com.example.sales_system.enums.*;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.PurchaseDetailMapper;
import com.example.sales_system.mapper.PurchaseMapper;
import com.example.sales_system.repository.tenant.PurchaseDetailRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PurchaseService {
    PurchaseRepository purchaseRepository;
    PurchaseDetailRepository purchaseDetailRepository;

    VendorService vendorService;
    ContractService contractService;
    StoreService storeService;
    EmployeeService employeeService;
    ProductService productService;

    PurchaseMapper purchaseMapper;
    PurchaseDetailMapper purchaseDetailMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<PurchaseResponse> getAllPurchases(Specification<Purchase> specification, Pageable pageable) {
        Page<Purchase> page = purchaseRepository.findAll(specification, pageable);

        return ListResponse.<PurchaseResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(purchaseMapper::toPurchase).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public PurchaseResponse getPurchase(Long id) {
        return purchaseMapper.toPurchase(getPurchaseById(id));
    }


    @Transactional(transactionManager = "tenantTransactionManager")
    public PurchaseResponse createPurchase(PurchaseCreateRequest request) {
        Purchase purchase = purchaseMapper.toPurchase(request);
        // vendor
        var vendor = vendorService.getVendorById(request.getVendorId());
        if (vendor.getStatus().equals(VendorStatus.INACTIVE))
            throw new AppException(AppStatusCode.PURCHASE_ON_VENDOR_MUST_BE_ACTIVE);
        purchase.setVendor(vendor);

        // contract
        if (purchase.isUseContract()) {
            var contract = contractService.getContractById(request.getContractId());
            if (contract.getStatus().equals(ContractStatus.INACTIVE))
                throw new AppException(AppStatusCode.PURCHASE_ON_CONTRACT_MUST_BE_ACTIVE);
            purchase.setContract(contract);
        }

        // store
        var store = storeService.getStoreById(request.getStoreId());
        if (store.getStatus().equals(StoreStatus.INACTIVE))
            throw new AppException(AppStatusCode.PURCHASE_ON_STORE_MUST_BE_ACTIVE);
        purchase.setStore(store);

        // employee
        var employee = employeeService.getEmployeeById(request.getEmployeeId());
        if (employee.getStatus().equals(EmployeeStatus.INACTIVE))
            throw new AppException(AppStatusCode.PURCHASE_ON_EMPLOYEE_MUST_BE_ACTIVE);
        purchase.setEmployee(employee);

        // status
        purchase.setStatus(PurchaseStatus.DRAFT);
        purchase.setReceiveStatus(ReceiveStatus.NOT_RECEIVED);
        purchase.setPaymentStatus(PaymentStatus.UNPAID);

        // Details
        var details = request.getDetails().stream()
                .map(detailRequest -> {
                    var detail = purchaseDetailMapper.toPurchaseDetail(detailRequest);
                    var product = productService.getProductById(detailRequest.getProductId());
                    if (product.getStatus().equals(ProductStatus.INACTIVE))
                        throw new AppException(AppStatusCode.PURCHASE_ON_PRODUCT_MUST_BE_ACTIVE);
                    detail.setProduct(product);
                    detail.setSubTotal(detail.getPurchasePrice() * detail.getPurchaseAmount());
                    return detail;
                })
                .toList();

        if (details.isEmpty()) {
            throw new AppException(AppStatusCode.INVALID_PURCHASE_DETAILS);
        }

        // calculate total
        Long total = details.stream().reduce(0L, (acc, ele) -> acc + ele.getSubTotal(), Long::sum);
        purchase.setTotal(total);

        // save
        purchase.setDetails(new HashSet<>(details)); // may be empty at return
        Purchase finalPurchase = purchaseRepository.save(purchase);
        // save details
        details.forEach(detail -> {
            detail.setPurchase(finalPurchase);
            purchaseDetailRepository.save(detail);
        });

        return purchaseMapper.toPurchase(finalPurchase);
    }

    // ----- Helper functions -----

    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.PURCHASE_NOT_FOUND));
    }

}
