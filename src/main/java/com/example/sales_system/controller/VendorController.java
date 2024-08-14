package com.example.sales_system.controller;

import com.example.sales_system.dto.request.VendorCreateRequest;
import com.example.sales_system.dto.request.VendorUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.VendorResponse;
import com.example.sales_system.entity.tenant.Vendor;
import com.example.sales_system.enums.VendorStatus;
import com.example.sales_system.service.VendorService;
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
@RequestMapping("/vendors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Vendor", description = "The Vendor API.")
public class VendorController {
    VendorService vendorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ListResponse<VendorResponse>> getAllVendors(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) VendorStatus status,
            @RequestParam(required = false) String note
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Vendor.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Vendor>()
                .or("fullName", FilterOperator.LIKE, search)
                .or("email", FilterOperator.LIKE, search)
                .or("phone", FilterOperator.LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Vendor>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("fullName", FilterOperator.LIKE, fullName)
                .and("email", FilterOperator.LIKE, email)
                .and("phone", FilterOperator.LIKE, phone)
                .and("address", FilterOperator.LIKE, address)
                .and("province", FilterOperator.LIKE, province)
                .and("district", FilterOperator.LIKE, district)
                .and("status", FilterOperator.EQUAL, status)
                .and("note", FilterOperator.LIKE, note)
                .build();

        return AppResponse.<ListResponse<VendorResponse>>builder()
                .result(vendorService.getAllVendors(spec, pageable))
                .build();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<VendorResponse> getVendorById(@PathVariable Long id) {
        return AppResponse.<VendorResponse>builder()
                .result(vendorService.getVendorResponse(id))
                .build();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public AppResponse<VendorResponse> createVendor(@RequestBody @Valid VendorCreateRequest request) {
        return AppResponse.<VendorResponse>builder()
                .result(vendorService.createVendor(request))
                .build();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<VendorResponse> updateVendor(@PathVariable Long id, @RequestBody @Valid VendorUpdateRequest request) {
        return AppResponse.<VendorResponse>builder()
                .result(vendorService.updateVendor(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
    }
}
