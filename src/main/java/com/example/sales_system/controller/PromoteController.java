package com.example.sales_system.controller;

import com.example.sales_system.dto.request.PromoteCreateRequest;
import com.example.sales_system.dto.request.PromoteUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.PromoteResponse;
import com.example.sales_system.entity.tenant.Promote;
import com.example.sales_system.enums.PromoteStatus;
import com.example.sales_system.service.PromoteService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.specification.PromoteSpecifications;
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
@RequestMapping("/promotes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Promote", description = "The Promote API.")
public class PromoteController {
    PromoteService promoteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROMOTE')")
    public AppResponse<ListResponse<PromoteResponse>> getAllPromotes(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) PromoteStatus status,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long storeId
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Promote.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Promote>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .or("name", FilterOperator.LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Promote>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("name", FilterOperator.LIKE, name)
                .and("type", FilterOperator.EQUAL, type)
                .and("status", FilterOperator.EQUAL, status)
                .and(PromoteSpecifications.hasEmployee(employeeId))
                .and(PromoteSpecifications.hasProduct(productId))
                .and(PromoteSpecifications.hasStore(storeId))
                .build();

        return AppResponse.<ListResponse<PromoteResponse>>builder()
                .result(promoteService.getAllPromotes(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROMOTE')")
    public AppResponse<PromoteResponse> getPromoteById(@PathVariable Long id) {
        return AppResponse.<PromoteResponse>builder()
                .result(promoteService.getPromote(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROMOTE')")
    public AppResponse<PromoteResponse> createPromote(@RequestBody @Valid PromoteCreateRequest request) {
        return AppResponse.<PromoteResponse>builder()
                .result(promoteService.createPromote(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROMOTE')")
    public AppResponse<PromoteResponse> updatePromote(@PathVariable Long id, @RequestBody @Valid PromoteUpdateRequest request) {
        return AppResponse.<PromoteResponse>builder()
                .result(promoteService.updatePromote(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROMOTE')")
    public void deletePromote(@PathVariable Long id) {
        promoteService.deletePromote(id);
    }
}
