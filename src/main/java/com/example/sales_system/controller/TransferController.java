package com.example.sales_system.controller;

import com.example.sales_system.dto.request.TransferUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Inventory;
import com.example.sales_system.entity.tenant.Transfer;
import com.example.sales_system.entity.tenant.TransferCreationRequest;
import com.example.sales_system.entity.tenant.TransferResponse;
import com.example.sales_system.service.TransferService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.specification.InventorySpecifications;
import com.example.sales_system.specification.TransferSpecifications;
import com.example.sales_system.util.SortBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Transfer", description = "The Transfer API.")
public class TransferController {
    TransferService transferService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('TRANSFER')")
    public AppResponse<ListResponse<TransferResponse>> getAllTransfers(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) Long fromStoreId,
            @RequestParam(required = false) Long toStoreId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long productId
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Transfer.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Transfer>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .build();

        var spec = new FilterSpecificationBuilder<Transfer>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("note", FilterOperator.LIKE, note)
                .and(TransferSpecifications.hasFromStore(fromStoreId))
                .and(TransferSpecifications.hasToStore(toStoreId))
                .and(TransferSpecifications.hasEmployee(employeeId))
                .and(TransferSpecifications.hasProduct(productId))
                .build();

        return AppResponse.<ListResponse<TransferResponse>>builder()
                .result(transferService.getAll(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<TransferResponse> getById(@PathVariable Long id) {
        return AppResponse.<TransferResponse>builder()
                .result(transferService.getById(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public AppResponse<TransferResponse> create(@RequestBody @Valid TransferCreationRequest request) {
        return AppResponse.<TransferResponse>builder()
                .result(transferService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<TransferResponse> update(@PathVariable Long id, @RequestBody @Valid TransferUpdateRequest request) {
        return AppResponse.<TransferResponse>builder()
                .result(transferService.update(id, request))
                .build();
    }

}
