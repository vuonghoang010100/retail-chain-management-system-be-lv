package com.example.sales_system.controller;

import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.entity.tenant.Batch;
import com.example.sales_system.service.BatchService;
import com.example.sales_system.specification.BatchSpecifications;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.util.SortBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/batches")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Batch", description = "The Batch API.")
public class BatchController {
    BatchService batchService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<Object> getAllBatches(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Long storeId
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Batch.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Batch>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .or(BatchSpecifications.hasProductName(search))
                .or(BatchSpecifications.hasProductCode(search))
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Batch>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and(BatchSpecifications.hasProductId(productId))
                .and(BatchSpecifications.hasProductName(productName))
                .and(BatchSpecifications.hasProductCode(sku))
                .and(BatchSpecifications.hasStoreId(storeId))
                .build();

        return AppResponse.builder()
                .result(batchService.getAllBatches(spec, pageable))
                .build();
    }

}
