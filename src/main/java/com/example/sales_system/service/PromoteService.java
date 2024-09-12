package com.example.sales_system.service;

import com.example.sales_system.dto.request.PromoteCreateRequest;
import com.example.sales_system.dto.request.PromoteUpdateRequest;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.PromoteResponse;
import com.example.sales_system.entity.tenant.Promote;
import com.example.sales_system.enums.PromoteStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.PromoteMapper;
import com.example.sales_system.repository.tenant.ProductRepository;
import com.example.sales_system.repository.tenant.PromoteRepository;
import com.example.sales_system.repository.tenant.StoreRepository;
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
public class PromoteService {
    PromoteRepository promoteRepository;
    ProductRepository productRepository;
    StoreRepository storeRepository;

    PromoteMapper promoteMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<PromoteResponse> getAllPromotes(Specification<Promote> specification, Pageable pageable) {
        Page<Promote> page = promoteRepository.findAll(specification, pageable);

        return ListResponse.<PromoteResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(promoteMapper::toPromoteResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public PromoteResponse getPromote(Long id) {
        return promoteMapper.toPromoteResponse(getPromoteById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public PromoteResponse createPromote(PromoteCreateRequest request) {
        Promote promote = promoteMapper.toPromote(request);

        // Status
        if (Objects.isNull(promote.getStatus()))
            promote.setStatus(PromoteStatus.ACTIVE);

        // product
        if (!promote.getAllProduct()) {
            var products = productRepository.findAllById(request.getProductIds());

            if (products.isEmpty())
                throw new AppException(AppStatusCode.PRODUCT_LIST_MUST_NOT_BE_EMPTY);

            promote.setProducts(new HashSet<>(products));
        } else {
            promote.setProducts(new HashSet<>(productRepository.findAll()));
        }

        // store
        if (!promote.getAllStore()) {
            var stores = storeRepository.findAllById(request.getStoreIds());

            if (stores.isEmpty())
                throw new AppException(AppStatusCode.STORE_LIST_MUST_NOT_BE_EMPTY);

            promote.setStores(new HashSet<>(stores));
        } else {
            promote.setStores(new HashSet<>(storeRepository.findAll()));
        }

        promote = promoteRepository.save(promote);

        return promoteMapper.toPromoteResponse(promote);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public PromoteResponse updatePromote(Long id, PromoteUpdateRequest request) {
        var promote = getPromoteById(id);
        promoteMapper.updatePromote(promote, request);

        // product
        if (!promote.getAllProduct()) {
            var products = productRepository.findAllById(request.getProductIds());

            if (products.isEmpty())
                throw new AppException(AppStatusCode.PRODUCT_LIST_MUST_NOT_BE_EMPTY);

            promote.setProducts(new HashSet<>(products));
        } else {
            promote.setProducts(new HashSet<>(productRepository.findAll()));
        }

        // store
        if (!promote.getAllStore()) {
            var stores = storeRepository.findAllById(request.getStoreIds());

            if (stores.isEmpty())
                throw new AppException(AppStatusCode.STORE_LIST_MUST_NOT_BE_EMPTY);

            promote.setStores(new HashSet<>(stores));
        } else {
            promote.setStores(new HashSet<>(storeRepository.findAll()));
        }

        promote = promoteRepository.save(promote);
        return promoteMapper.toPromoteResponse(promote);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public void deletePromote(Long id) {
    }

    // ----- Helper functions -----

    public Promote getPromoteById(Long id) {
        return promoteRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.PROMOTE_NOT_FOUND));
    }
}
