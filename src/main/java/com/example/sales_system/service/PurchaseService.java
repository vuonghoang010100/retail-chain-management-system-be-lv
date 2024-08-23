package com.example.sales_system.service;

import com.example.sales_system.dto.request.PurchaseCreateRequest;
import com.example.sales_system.dto.request.PurchaseReceiveRequest;
import com.example.sales_system.dto.request.PurchaseUpdateRequest;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.PurchaseResponse;
import com.example.sales_system.entity.tenant.Batch;
import com.example.sales_system.entity.tenant.Contract;
import com.example.sales_system.entity.tenant.Purchase;
import com.example.sales_system.enums.*;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.PurchaseDetailMapper;
import com.example.sales_system.mapper.PurchaseMapper;
import com.example.sales_system.repository.tenant.BatchRepository;
import com.example.sales_system.repository.tenant.ContractRepository;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PurchaseService {
    PurchaseRepository purchaseRepository;
    PurchaseDetailRepository purchaseDetailRepository;
    BatchRepository batchRepository;
    ContractRepository contractRepository;

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

    @Transactional(transactionManager = "tenantTransactionManager")
    public PurchaseResponse updatePurchase(Long id, PurchaseUpdateRequest request) {
        Purchase purchase = getPurchaseById(id);

        // Not update PENDING, COMPLETE
        var status = purchase.getStatus();
        if (status.equals(PurchaseStatus.COMPLETE)) {
            throw new AppException(AppStatusCode.CANT_NOT_UPDATE_PURCHASE);
        }

        // Update status
        if (request.getStatus().equals(PurchaseStatus.COMPLETE))
            throw new AppException(AppStatusCode.INVALID_UPDATE_PURCHASE_STATUS);
        purchase.setStatus(request.getStatus());

        // Update vendor
        var vendor = vendorService.getVendorById(request.getVendorId());
        if (vendor.getStatus().equals(VendorStatus.INACTIVE))
            throw new AppException(AppStatusCode.PURCHASE_ON_VENDOR_MUST_BE_ACTIVE);
        purchase.setVendor(vendor);

        // Update contract
        purchase.setUseContract(request.isUseContract());
        if (purchase.isUseContract()) {
            var contract = contractService.getContractById(request.getContractId());
            if (contract.getStatus().equals(ContractStatus.INACTIVE))
                throw new AppException(AppStatusCode.PURCHASE_ON_CONTRACT_MUST_BE_ACTIVE);
            purchase.setContract(contract);
        } else {
            purchase.setContract(null);
        }

        // Update store
        var store = storeService.getStoreById(request.getStoreId());
        if (store.getStatus().equals(StoreStatus.INACTIVE))
            throw new AppException(AppStatusCode.PURCHASE_ON_STORE_MUST_BE_ACTIVE);
        purchase.setStore(store);

        // employee
        var employee = employeeService.getEmployeeById(request.getEmployeeId());
        if (employee.getStatus().equals(EmployeeStatus.INACTIVE))
            throw new AppException(AppStatusCode.PURCHASE_ON_EMPLOYEE_MUST_BE_ACTIVE);
        purchase.setEmployee(employee);

        // Update details
        var details = purchase.getDetails()
                .stream()
                .map(detail -> {
                    var detailRequest = request.getDetails().stream().filter(ele -> ele.getId().equals(detail.getId())).findFirst();
                    if (detailRequest.isEmpty())
                        throw new AppException(AppStatusCode.INVALID_PURCHASE_DETAILS);
                    detail.setPurchasePrice(detailRequest.get().getPurchasePrice());
                    detail.setPurchaseAmount(detailRequest.get().getPurchaseAmount());
                    detail.setSubTotal(detail.getPurchasePrice() * detail.getPurchaseAmount());
                    return detail;
                })
                .toList();
        purchase.setDetails(new HashSet<>(details));

        // calculate total
        Long total = details.stream().reduce(0L, (acc, ele) -> acc + ele.getSubTotal(), Long::sum);
        purchase.setTotal(total);

        // save
        Purchase finalPurchase = purchaseRepository.save(purchase);

        // save details
        details.forEach(detail -> {
            detail.setPurchase(finalPurchase);
            purchaseDetailRepository.save(detail);
        });

        return purchaseMapper.toPurchase(finalPurchase);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public PurchaseResponse receivePurchase(Long id, PurchaseReceiveRequest request) {
        Purchase purchase = getPurchaseById(id);

        if (!purchase.getStatus().equals(PurchaseStatus.PENDING))
            throw new AppException(AppStatusCode.ONLY_RECEIVE_PURCHASE_WITH_STATUS_PENDING);

        // Update status
        if (request.getReceiveStatus().equals(ReceiveStatus.NOT_RECEIVED))
            throw new AppException(AppStatusCode.PURCHASE_RECEIVE_MUST_NOT_BE_NOT_RECEIVED);
        purchase.setReceiveStatus(request.getReceiveStatus());

        purchase.setStatus(PurchaseStatus.COMPLETE);

        // Date
        purchase.setReceivedDate(LocalDate.now());

        // Update details
        var details = purchase.getDetails().stream()
                .map(detail -> {
                    var detailRequest = request.getDetails().stream().filter(ele -> ele.getId().equals(detail.getId())).findFirst();
                    if (detailRequest.isEmpty())
                        throw new AppException(AppStatusCode.INVALID_PURCHASE_DETAILS);
                    if (purchase.getReceiveStatus().equals(ReceiveStatus.RECEIVED)) {
                        detail.setReceivedAmount(detail.getPurchaseAmount());
                    } else {
                        detail.setReceivedAmount(detailRequest.get().getReceivedAmount());
                    }
                    detail.setSubTotal(detail.getReceivedAmount() * detail.getPurchasePrice());

                    detail.setMfg(detailRequest.get().getMfg());
                    detail.setExp(detailRequest.get().getExp());

                    return detail;
                })
                .toList();

        // Create batch
        details.forEach(detail -> {
            Batch batch = Batch.builder()
                    .quantity(detail.getReceivedAmount())
                    .purchaseAmount(detail.getSubTotal())
                    .purchasePrice(detail.getPurchasePrice())
                    .mfg(detail.getMfg())
                    .exp(detail.getExp())
                    .store(purchase.getStore())
                    .product(detail.getProduct())
                    .build();

            // save batch
            batch = batchRepository.save(batch);

            detail.setBatch(batch);
        });

        // save
        purchase.setDetails(new HashSet<>(details));
        Purchase finalPurchase = purchaseRepository.save(purchase);

        // save details
        details.forEach(detail -> {
            detail.setPurchase(finalPurchase);
            purchaseDetailRepository.save(detail);
        });

        // update contract TODO: move to contract service
        if (purchase.isUseContract()) {
            Contract contract = purchase.getContract();
            contract.setLatestPurchaseDate(LocalDate.now());
            contract.setNextPurchaseDate(LocalDate.now().plusDays(contract.getPeriod()));

            contractRepository.save(contract);
        }

        return purchaseMapper.toPurchase(finalPurchase);
    }

    // ----- Helper functions -----

    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.PURCHASE_NOT_FOUND));
    }

}
