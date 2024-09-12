package com.example.sales_system.service;

import com.example.sales_system.entity.tenant.Batch;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.repository.tenant.BatchRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BatchService {
    BatchRepository batchRepository;


    public Batch getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.BATCH_NOT_FOUND));
    }

    public Batch updateBatch(Batch batch) {
        return batchRepository.save(batch);
    }
}
