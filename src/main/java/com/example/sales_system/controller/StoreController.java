package com.example.sales_system.controller;

import com.example.sales_system.dto.request.StoreCreateRequest;
import com.example.sales_system.dto.request.StoreUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.StoreResponse;
import com.example.sales_system.entity.tenant.Store;
import com.example.sales_system.enums.StoreStatus;
import com.example.sales_system.service.EmployeeService;
import com.example.sales_system.service.StoreService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Store", description = "The Store API.")
public class StoreController {
    StoreService storeService;
    EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STORE')")
    public AppResponse<ListResponse<StoreResponse>> getAllStores(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) StoreStatus status,
            @RequestParam(required = false) String note
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Store.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Store>()
                .or("name", FilterOperator.LIKE, search)
                .or("fullName", FilterOperator.LIKE, search)
                .build();

        var spec = new FilterSpecificationBuilder<Store>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("name", FilterOperator.LIKE, name)
                .and("fullName", FilterOperator.LIKE, fullName)
                .and("address", FilterOperator.LIKE, address)
                .and("province", FilterOperator.LIKE, province)
                .and("district", FilterOperator.LIKE, district)
                .and("email", FilterOperator.LIKE, email)
                .and("phone", FilterOperator.LIKE, phone)
                .and("status", FilterOperator.EQUAL, status)
                .and("note", FilterOperator.LIKE, note)
                .build();

        return AppResponse.<ListResponse<StoreResponse>>builder()
                .result(storeService.getAllStoreResponses(spec, pageable))
                .build();
    }

    @GetMapping("/work")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STORE')")
    public AppResponse<List<StoreResponse>> getAllStores2(
    ) {
        return AppResponse.<List<StoreResponse>>builder()
                .result(storeService.getAllWorkStore())
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STORE')")
    public AppResponse<StoreResponse> getStoreById(@PathVariable Long id) {
        return AppResponse.<StoreResponse>builder()
                .result(storeService.getStoreResponse(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STORE')")
    public AppResponse<StoreResponse> createStore(@RequestBody @Valid StoreCreateRequest request) {
        return AppResponse.<StoreResponse>builder()
                .result(storeService.createStore(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STORE')")
    public AppResponse<StoreResponse> updateStore(@PathVariable Long id, @RequestBody @Valid StoreUpdateRequest request) {
        return AppResponse.<StoreResponse>builder()
                .result(storeService.updateStore(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STORE')")
    public void deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
    }

}
