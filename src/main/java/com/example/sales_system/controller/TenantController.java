package com.example.sales_system.controller;


import com.example.sales_system.configuration.multitenant.TenantIdentifierResolver;
import com.example.sales_system.dto.request.TenantCreateRequest;
import com.example.sales_system.dto.response.TenantCreateResponse;
import com.example.sales_system.entity.Employee;
import com.example.sales_system.entity.Tenant;
import com.example.sales_system.service.EmployeeService;
import com.example.sales_system.service.TenantService;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantController {
    TenantService tenantService;
    EmployeeService employeeService;
    EntityManager entityManager;

    TenantIdentifierResolver tenantIdentifierResolver;

    @PostMapping("/regis")
    @Transactional
    @Modifying
    public ResponseEntity<TenantCreateResponse> createTenantWithAdminUser(@RequestBody TenantCreateRequest request) {
//        tenantIdentifierResolver.setCurrentTenant(TenantIdentifierResolver.DEFAULT_TENANT);
        entityManager
                .createNativeQuery("SET SCHEMA '%s'".formatted(TenantIdentifierResolver.DEFAULT_TENANT))
                .executeUpdate();

        Tenant tenant;
        try {
            tenant = tenantService.createTenantAndSchema(request.getTenantId());
        } catch (RuntimeException ignored) {
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

}
