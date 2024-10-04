package com.example.sales_system.controller;

import com.example.sales_system.dto.request.ContractCreateRequest;
import com.example.sales_system.dto.request.ContractUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ContractResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Contract;
import com.example.sales_system.enums.ContractStatus;
import com.example.sales_system.service.ContractService;
import com.example.sales_system.specification.ContractSpecifications;
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
@RequestMapping("/contracts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Contract", description = "The Contract API.")
public class ContractController {
    ContractService contractService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CONTRACT')")
    public AppResponse<ListResponse<ContractResponse>> getAllContracts(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) ContractStatus status,
            @RequestParam(required = false) Long vendorId
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Contract.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Contract>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .or(ContractSpecifications.hasVendorfullNameLike(search))
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Contract>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("status", FilterOperator.EQUAL, status)
                .and(ContractSpecifications.hasVendorId(vendorId))
                .build();

        return AppResponse.<ListResponse<ContractResponse>>builder()
                .result(contractService.getAllContracts(spec, pageable))
                .build();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CONTRACT')")
    public AppResponse<ContractResponse> getContractById(@PathVariable Long id) {
        return AppResponse.<ContractResponse>builder()
                .result(contractService.getContract(id))
                .build();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CONTRACT')")
    public AppResponse<ContractResponse> createContract(@RequestBody @Valid ContractCreateRequest request) {
        return AppResponse.<ContractResponse>builder()
                .result(contractService.createContract(request))
                .build();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CONTRACT')")
    public AppResponse<ContractResponse> updateContract(@PathVariable Long id, @RequestBody @Valid ContractUpdateRequest request) {
        return AppResponse.<ContractResponse>builder()
                .result(contractService.updateContract(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CONTRACT')")
    public void deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
    }
}
