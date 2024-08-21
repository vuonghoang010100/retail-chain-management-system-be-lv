package com.example.sales_system.service;

import com.example.sales_system.dto.request.PurchaseCreateRequest;
import com.example.sales_system.dto.response.PurchaseResponse;
import com.example.sales_system.entity.tenant.Purchase;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

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


    @Transactional(transactionManager = "tenantTransactionManager")
    public PurchaseResponse createPurchase(PurchaseCreateRequest request) {
        Purchase purchase = purchaseMapper.toPurchase(request);
        // vendor
        var vendor = vendorService.getVendorById(request.getVendorId());
        purchase.setVendor(vendor);

        // contract
        if (purchase.isUseContract()) {
            var contract = contractService.getContractById(request.getContractId());
            purchase.setContract(contract);
        }

        // store
        var store = storeService.getStoreById(request.getStoreId());
        purchase.setStore(store);

        // employee
        var employee = employeeService.getEmployeeById(request.getEmployeeId());
        purchase.setEmployee(employee);

        // Details
        var details = request.getDetails().stream().map(purchaseDetailMapper::toPurchaseDetail).toList();
        details.forEach(detail -> {
            var product = productService.getProductById(detail.getId());
            detail.setProduct(product);
            detail.setSubTotal(detail.getPurchasePrice() * detail.getPurchaseAmount());
        });

        if (details.isEmpty()) {
            throw new AppException(AppStatusCode.INVALID_PURCHASE_DETAILS);
        }

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

}
