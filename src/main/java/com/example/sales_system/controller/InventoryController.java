package com.example.sales_system.controller;

import com.example.sales_system.dto.request.InventoryCreateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.InventoryResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Inventory;
import com.example.sales_system.service.InventoryService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.specification.InventorySpecifications;
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
@RequestMapping("/inventory_checks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "InventoryCheck", description = "The InventoryCheck API.")
public class InventoryController {
    InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ListResponse<InventoryResponse>> getAllInventory(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long productId
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Inventory.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Inventory>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Inventory>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and(InventorySpecifications.hasStore(storeId))
                .and(InventorySpecifications.hasEmployee(employeeId))
                .and(InventorySpecifications.hasProduct(productId))
                .build();

        return AppResponse.<ListResponse<InventoryResponse>>builder()
                .result(inventoryService.getAllInventory(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<InventoryResponse> getInventory(@PathVariable Long id) {
        return AppResponse.<InventoryResponse>builder()
                .result(inventoryService.getInventory(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public AppResponse<InventoryResponse> createInventory(@RequestBody @Valid InventoryCreateRequest request) {
        return AppResponse.<InventoryResponse>builder()
                .result(inventoryService.createInventory(request))
                .build();
    }
}
