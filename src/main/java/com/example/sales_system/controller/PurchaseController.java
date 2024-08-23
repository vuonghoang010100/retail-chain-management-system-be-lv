package com.example.sales_system.controller;

import com.example.sales_system.dto.request.PurchaseCreateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.PurchaseResponse;
import com.example.sales_system.entity.tenant.Purchase;
import com.example.sales_system.service.PurchaseService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
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
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Purchase", description = "The Purchase API.")
public class PurchaseController {
    PurchaseService purchaseService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ListResponse<PurchaseResponse>> getAllPurchases(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Purchase.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Purchase>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)

                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Purchase>()
                .and(searchSpec)

                .build();

        return AppResponse.<ListResponse<PurchaseResponse>>builder()
                .result(purchaseService.getAllPurchases(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<PurchaseResponse> getPurchaseById(@PathVariable Long id) {
        return AppResponse.<PurchaseResponse>builder()
                .result(purchaseService.getPurchase(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public AppResponse<PurchaseResponse> createPurchase(@RequestBody @Valid PurchaseCreateRequest request) {
        return AppResponse.<PurchaseResponse>builder()
                .result(purchaseService.createPurchase(request))
                .build();
    }
}
