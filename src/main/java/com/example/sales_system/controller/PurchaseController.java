package com.example.sales_system.controller;

import com.example.sales_system.dto.request.PurchaseCreateRequest;
import com.example.sales_system.dto.request.PurchaseReceiveRequest;
import com.example.sales_system.dto.request.PurchaseUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.PurchaseResponse;
import com.example.sales_system.entity.tenant.Purchase;
import com.example.sales_system.enums.PaymentStatus;
import com.example.sales_system.enums.PurchaseStatus;
import com.example.sales_system.enums.ReceiveStatus;
import com.example.sales_system.service.PurchaseService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.specification.PurchaseSpecifications;
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

import java.time.LocalDate;

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
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Boolean useContract,
            @RequestParam(required = false) LocalDate receivedDate,
            @RequestParam(required = false) LocalDate gteReceivedDate,
            @RequestParam(required = false) LocalDate lteReceivedDate,
            @RequestParam(required = false) PurchaseStatus status,
            @RequestParam(required = false) ReceiveStatus receiveStatus,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) Long total,
            @RequestParam(required = false) Long gteTotal,
            @RequestParam(required = false) Long lteTotal,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) Long contractId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long productId
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
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("useContract", FilterOperator.EQUAL, useContract)
                .and("receivedDate", FilterOperator.EQUAL, receivedDate)
                .and("receivedDate", FilterOperator.GTE_DATE, gteReceivedDate)
                .and("receivedDate", FilterOperator.LTE_DATE, lteReceivedDate)
                .and("status", FilterOperator.EQUAL, status)
                .and("receiveStatus", FilterOperator.EQUAL, receiveStatus)
                .and("paymentStatus", FilterOperator.EQUAL, paymentStatus)
                .and("total", FilterOperator.EQUAL, total)
                .and("total", FilterOperator.GTE_LONG, gteTotal)
                .and("total", FilterOperator.LTE_LONG, lteTotal)
                .and(PurchaseSpecifications.hasVendor(vendorId))
                .and(PurchaseSpecifications.hasContract(contractId))
                .and(PurchaseSpecifications.hasStore(storeId))
                .and(PurchaseSpecifications.hasEmployee(employeeId))
                .and(PurchaseSpecifications.hasProduct(productId))

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


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<PurchaseResponse> updatePurchase(@PathVariable Long id, @RequestBody @Valid PurchaseUpdateRequest request) {
        return AppResponse.<PurchaseResponse>builder()
                .result(purchaseService.updatePurchase(id, request))
                .build();
    }


    @PostMapping("/{id}/receive")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<PurchaseResponse> receivePurchase(@PathVariable Long id, @RequestBody @Valid PurchaseReceiveRequest request) {
        return AppResponse.<PurchaseResponse>builder()
                .result(purchaseService.receivePurchase(id, request))
                .build();
    }
}
