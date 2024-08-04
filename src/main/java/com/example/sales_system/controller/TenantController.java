package com.example.sales_system.controller;

import com.example.sales_system.configuration.TenantContext;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.TenantResponse;
import com.example.sales_system.entity.master.Tenant;
import com.example.sales_system.service.TenantService;
import com.example.sales_system.service.UserSevice;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantController {
    TenantService tenantService;
    private final UserSevice userSevice;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public AppResponse<List<TenantResponse>> getAllTenants() {
        return AppResponse.<List<TenantResponse>>builder()
                .result(tenantService.getAllTenants())
                .build();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public AppResponse<?> activeTenants(@RequestParam Long tenantId) {
        // Note: business logic here
        // because @Tranactional with diffence TransactionManager not working together in @Service class
        // Without @Tranactional, throw TransactionRequiredException
        Tenant tenant = tenantService.getTenant(tenantId);
        if (!tenant.getInitStatus()) {
            // force drop chema
            // DROP SCHEMA IF EXISTS %name% CASCADE

            tenantService.createSchema(tenant.getName());
            // exception : SQLGrammarException : JDBC exception executing SQL [CREATE SCHEMA t_test] [ERROR: schema "t_test" already exists]
            // timeout -> delete schema

            tenantService.initTenantTables(tenant);
            // exception: ScriptStatementFailedException : Failed to execute SQL script

            // PSQLException: ERROR: schema "ttest1" already exists
            // PSQLException: ERROR: relation "employee" already exists

            TenantContext.setTenantId(tenant.getName());

            tenantService.initTenantDatas(tenant);

            tenantService.active(tenant);
        }

        return AppResponse.builder().build();
    }


}
