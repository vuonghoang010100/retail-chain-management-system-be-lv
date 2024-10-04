package com.example.sales_system.controller;

import com.example.sales_system.dto.request.CustomerCreateRequest;
import com.example.sales_system.dto.request.CustomerUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.CustomerResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Customer;
import com.example.sales_system.enums.Gender;
import com.example.sales_system.service.CustomerService;
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

import java.time.LocalDate;

@CrossOrigin("*")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Customer", description = "The Customer API.")
public class CustomerController {
    CustomerService customerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CUSTOMER')")
    public AppResponse<ListResponse<CustomerResponse>> getAllCustomers(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) LocalDate dob,
            @RequestParam(required = false) LocalDate fromDob,
            @RequestParam(required = false) LocalDate toDob,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Long rewardPoint,
            @RequestParam(required = false) Long gteRewardPoint,
            @RequestParam(required = false) Long lteRewardPoint,
            @RequestParam(required = false) String note
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Customer.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Customer>()
                .or("fullName", FilterOperator.LIKE, search)
                .or("phone", FilterOperator.LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Customer>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("fullName", FilterOperator.LIKE, fullName)
                .and("dob", FilterOperator.EQUAL, dob)
                .and("dob", FilterOperator.GTE_DATE, fromDob)
                .and("dob", FilterOperator.LTE_DATE, toDob)
                .and("gender", FilterOperator.EQUAL, gender)
                .and("email", FilterOperator.LIKE, email)
                .and("phone", FilterOperator.LIKE, phone)
                .and("address", FilterOperator.LIKE, address)
                .and("province", FilterOperator.LIKE, province)
                .and("district", FilterOperator.LIKE, district)
                .and("rewardPoint", FilterOperator.EQUAL, rewardPoint)
                .and("rewardPoint", FilterOperator.GTE_LONG, gteRewardPoint)
                .and("rewardPoint", FilterOperator.LTE_LONG, lteRewardPoint)
                .and("note", FilterOperator.LIKE, note)
                .build();

        return AppResponse.<ListResponse<CustomerResponse>>builder()
                .result(customerService.getAllCustomerResponses(spec, pageable))
                .build();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CUSTOMER')")
    public AppResponse<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return AppResponse.<CustomerResponse>builder()
                .result(customerService.getCustomerResponse(id))
                .build();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CUSTOMER')")
    public AppResponse<CustomerResponse> createCustomer(@RequestBody @Valid CustomerCreateRequest request) {
        return AppResponse.<CustomerResponse>builder()
                .result(customerService.createCustomer(request))
                .build();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CUSTOMER')")
    public AppResponse<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerUpdateRequest request) {
        return AppResponse.<CustomerResponse>builder()
                .result(customerService.updateCustomer(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CUSTOMER')")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

}
