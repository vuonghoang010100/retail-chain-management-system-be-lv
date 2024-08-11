package com.example.sales_system.service;

import com.example.sales_system.dto.request.StoreCreateRequest;
import com.example.sales_system.dto.request.StoreUpdateRequest;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.StoreResponse;
import com.example.sales_system.entity.tenant.Store;
import com.example.sales_system.enums.StoreStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.StoreMapper;
import com.example.sales_system.repository.tenant.EmployeeRepository;
import com.example.sales_system.repository.tenant.StoreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StoreService {
    private final StoreMapper storeMapper;
    StoreRepository storeRepository;
    EmployeeRepository employeeRepository;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<StoreResponse> getAllStoreResponses(Specification<Store> spec, Pageable pageable) {
        log.debug("getAllStoreResponses called with spec {}, pageable {}", spec, pageable);

        Page<Store> storePage = storeRepository.findAll(spec, pageable);

        return ListResponse.<StoreResponse>builder()
                .size(storePage.getSize())
                .page(storePage.getNumber() + 1)
                .total(storePage.getTotalElements())
                .numOfElements(storePage.getNumberOfElements())
                .totalPages(storePage.getTotalPages())
                .data(storePage.getContent().stream().map(storeMapper::toStoreResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public StoreResponse getStoreResponse(Long id) {
        log.debug("getStoreResponse called with id {}", id);
        return storeMapper.toStoreResponse(getStoreById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public StoreResponse createStore(StoreCreateRequest request) {
        log.debug("createStore called with request {}", request);
        Store store = storeMapper.toStore(request);
        // init status
        store.setStatus(StoreStatus.ACTIVE);
        // save
        store = saveStore(store);
        // update employee work on all store
        var employees = employeeRepository.findAllByAllStore(true);
        Store finalStore = store;
        employees.forEach(employee -> {
            var empStores = employee.getStores();
            empStores.add(finalStore);
            employee.setStores(empStores);
            employeeRepository.save(employee);
        });

        return storeMapper.toStoreResponse(store);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public StoreResponse updateStore(Long id, StoreUpdateRequest request) {
        log.debug("updateStore called with id {}", id);
        Store store = getStoreById(id);
        storeMapper.updateStore(store, request);

        return storeMapper.toStoreResponse(saveStore(store));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public void deleteStore(Long id) {
        log.debug("deleteStore called with id {}", id);
        Store store = getStoreById(id);
        storeRepository.delete(store);
    }


    // ----- Helper functions -----
    private Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.STORE_NOT_FOUND));
    }

    private Store saveStore(Store store) {
        // TODO: remove, also product, category, employee service
        try {
            store = storeRepository.save(store);
        } catch (DataIntegrityViolationException exception) {
            log.error(exception.getMessage());
            if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (email)")) {
                throw new AppException(AppStatusCode.EMAIL_ALREADY_EXISTED);
            } else if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (phone)")) {
                throw new AppException(AppStatusCode.PHONE_ALREADY_EXISTED);
            } else if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (name)")) {
                throw new AppException(AppStatusCode.NAME_ALREADY_EXISTED);
            } else if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (full_name)")) {
                throw new AppException(AppStatusCode.FULLNAME_ALREADY_EXISTED);
            }
            throw exception;
        }

        return store;
    }

}
