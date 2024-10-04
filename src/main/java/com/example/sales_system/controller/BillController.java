package com.example.sales_system.controller;

import com.example.sales_system.dto.request.BillCreateRequest;
import com.example.sales_system.dto.request.BillUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.BillResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Bill;
import com.example.sales_system.enums.PaymentStatus;
import com.example.sales_system.service.BillService;
import com.example.sales_system.specification.BillSpecifications;
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
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Bill", description = "The Bill API.")
public class BillController {
    BillService billService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PURCHASE')")
    public AppResponse<ListResponse<BillResponse>> getAllBills(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) Long total,
            @RequestParam(required = false) Long gteTotal,
            @RequestParam(required = false) Long lteTotal,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) Long employeeId
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Bill.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Bill>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Bill>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("paymentStatus", FilterOperator.EQUAL, paymentStatus)
                .and("total", FilterOperator.EQUAL, total)
                .and("total", FilterOperator.GTE_LONG, gteTotal)
                .and("total", FilterOperator.LTE_LONG, lteTotal)
                .and(BillSpecifications.hasVendor(vendorId))
                .and(BillSpecifications.hasEmployee(employeeId))
                .build();

        return AppResponse.<ListResponse<BillResponse>>builder()
                .result(billService.getAllBills(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PURCHASE')")
    public AppResponse<BillResponse> getBill(@PathVariable Long id) {
        return AppResponse.<BillResponse>builder()
                .result(billService.getBill(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PURCHASE')")
    public AppResponse<BillResponse> createBill(@RequestBody @Valid BillCreateRequest request) {
        return AppResponse.<BillResponse>builder()
                .result(billService.createBill(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PURCHASE')")
    public AppResponse<BillResponse> updateBill(@PathVariable Long id, @RequestBody @Valid BillUpdateRequest request) {
        return AppResponse.<BillResponse>builder()
                .result(billService.updateBill(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PURCHASE')")
    public void deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
    }

}
