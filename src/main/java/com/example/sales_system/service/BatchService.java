package com.example.sales_system.service;

import com.example.sales_system.dto.response.BatchResponse;
import com.example.sales_system.dto.response.BatchResponseSimple;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Batch;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.BatchMapper;
import com.example.sales_system.repository.tenant.BatchRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
public class BatchService {
    BatchRepository batchRepository;
    BatchMapper batchMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<BatchResponse> getAllBatches(Specification<Batch> spec, Pageable pageable) {
        Page<Batch> page = batchRepository.findAll(spec, pageable);

        return ListResponse.<BatchResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(batchMapper::toBatchResponse).collect(Collectors.toList()))
                .build();
    }


    public Batch getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.BATCH_NOT_FOUND));
    }

    public Batch updateBatch(Batch batch) {
        return batchRepository.save(batch);
    }
}
