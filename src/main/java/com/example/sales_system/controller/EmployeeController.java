package com.example.sales_system.controller;

import com.example.sales_system.dto.request.EmployeeCreateRequest;
import com.example.sales_system.dto.request.EmployeeUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.EmployeeResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.enums.Gender;
import com.example.sales_system.service.EmployeeService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.util.SortBuilder;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmployeeController {
    EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('EMPLOYEE_READ')")
    public AppResponse<ListResponse<EmployeeResponse>> getAllEmployees(
            @RequestParam(required = false, defaultValue = "0") int page,
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
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String note
    ) {
        log.debug("getAllEmployees called");

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Employee.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Employee>()
                .or("fullName", FilterOperator.LIKE, search)
                .or("email", FilterOperator.LIKE, search)
                .or("phone", FilterOperator.LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Employee>()
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
                .and("active", FilterOperator.EQUAL, active)
                .and("note", FilterOperator.LIKE, note)
                .build();


        return AppResponse.<ListResponse<EmployeeResponse>>builder()
                .result(employeeService.getAllEmployeeResponses(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('EMPLOYEE_READ')")
    public AppResponse<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        log.debug("getEmployeeById called");
        return AppResponse.<EmployeeResponse>builder()
                .result(employeeService.getEmployeeResponseById(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('EMPLOYEE_CREATE')")
    public AppResponse<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeCreateRequest request) {
        log.debug("createEmployee called");
        return AppResponse.<EmployeeResponse>builder()
                .result(employeeService.createEmployee(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('EMPLOYEE_UPDATE')")
    public AppResponse<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody @Valid EmployeeUpdateRequest request) {
        log.debug("updateEmployee called");
        return AppResponse.<EmployeeResponse>builder()
                .result(employeeService.updateEmployee(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('EMPLOYEE_DELETE')")
    public void deleteEmployee(@PathVariable Long id) {
        log.debug("deleteEmployee called");
        employeeService.deleteEmployeeById(id);
    }

}
