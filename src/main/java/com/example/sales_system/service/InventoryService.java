package com.example.sales_system.service;

import com.example.sales_system.dto.request.InventoryCreateRequest;
import com.example.sales_system.dto.response.InventoryResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Batch;
import com.example.sales_system.entity.tenant.Inventory;
import com.example.sales_system.entity.tenant.InventoryDetail;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.InventoryDetailMapper;
import com.example.sales_system.mapper.InventoryMapper;
import com.example.sales_system.repository.tenant.InventoryDetailRepository;
import com.example.sales_system.repository.tenant.InventoryRepository;
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
public class InventoryService {
    InventoryRepository inventoryRepository;
    InventoryDetailRepository inventoryDetailRepository;

    StoreService storeService;
    EmployeeService employeeService;
    BatchService batchService;

    InventoryMapper inventoryMapper;
    InventoryDetailMapper inventoryDetailMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<InventoryResponse> getAllInventory(Specification<Inventory> specification, Pageable pageable) {
        Page<Inventory> page = inventoryRepository.findAll(specification, pageable);

        return ListResponse.<InventoryResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(inventoryMapper::toResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public InventoryResponse getInventory(Long id) {
        return inventoryMapper.toResponse(getInventoryById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public InventoryResponse createInventory(InventoryCreateRequest request) {
        Inventory inventory = new Inventory();

        // employee
        var employee = employeeService.getEmployeeById(request.getEmployeeId());
        inventory.setEmployee(employee);

        // store
        var store = storeService.getStoreById(request.getStoreId());
        inventory.setStore(store);

        // details
        var details = request.getDetails()
                .stream()
                .map(detailRequest -> {
                    Batch batch = batchService.getBatchById(detailRequest.getBatchId());

                    if (!batch.getStore().getId().equals(store.getId())) {
                        throw new AppException(AppStatusCode.BATCH_MUST_BE_IN_THE_SAME_STORE);
                    }

                    return InventoryDetail.builder()
                            .product(batch.getProduct())
                            .batch(batch)
                            .realQuantity(detailRequest.getRealQuantity())
                            .dbQuantity(batch.getQuantity())
                            .differenceQuantity(detailRequest.getRealQuantity() - batch.getQuantity())
                            .build();
                })
                .toList();

        if (details.isEmpty())
            throw new AppException(AppStatusCode.INVENTORY_DETAIL_MUST_NOT_BE_EMPTY);

        // save
        inventory.setDetails(new HashSet<>(details));
        inventory = inventoryRepository.save(inventory);

        // save details
        Inventory finalInventory = inventory;
        details.forEach(detail -> {
            detail.setInventory(finalInventory);
            inventoryDetailRepository.save(detail);

            var batch = detail.getBatch();
            batch.setQuantity(detail.getRealQuantity());
            batchService.updateBatch(batch);
        });

        return inventoryMapper.toResponse(finalInventory);
    }


    // ----- Helper functions -----
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.INVENTORY_NOT_FOUND));
    }

}
