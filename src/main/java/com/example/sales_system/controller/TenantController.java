package com.example.sales_system.controller;


import com.example.sales_system.configuration.multitenant.TenantIdentifierResolver;
import com.example.sales_system.dto.request.TenantCreateRequest;
import com.example.sales_system.dto.response.TenantCreateResponse;
import com.example.sales_system.entity.Employee;
import com.example.sales_system.entity.Tenant;
import com.example.sales_system.service.EmployeeService;
import com.example.sales_system.service.TenantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tenants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantController {
    TenantService tenantService;
    EmployeeService employeeService;

    TenantIdentifierResolver tenantIdentifierResolver;

    @PostMapping("/regis")
    @Transactional
    @Modifying
    public ResponseEntity<TenantCreateResponse> createTenantWithAdminUser(@RequestBody TenantCreateRequest request) {
        // create tenant
        tenantIdentifierResolver.setCurrentTenant(TenantIdentifierResolver.DEFAULT_TENANT);


        Tenant tenant;
        try {
            tenant = tenantService.addTenantAndSchema(request.getTenantId());
        }
        catch (RuntimeException ignored) {
            log.error(ignored.getMessage());
            return ResponseEntity.badRequest().build();
        }

        // create user
        Employee employee = employeeService.createAdminEmployee(request.getUsername(), request.getPassword());

        return ResponseEntity.ok().body(
                TenantCreateResponse.builder()
                        .tenantId(tenant.getTenantId())
                        .username(employee.getUsername())
                        .build()
        );
    }

    @GetMapping("/{tenantId}")
    @ResponseStatus(HttpStatus.OK)
    public void test(@PathVariable String tenantId) {
        tenantIdentifierResolver.setCurrentTenant(tenantId);
        tenantService.createTenant(tenantId);
    }

}
