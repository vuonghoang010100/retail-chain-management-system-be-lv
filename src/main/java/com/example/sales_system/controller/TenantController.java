package com.example.sales_system.controller;


import com.example.sales_system.dto.request.TenantCreateRequest;
import com.example.sales_system.dto.response.TenantCreateResponse;
import com.example.sales_system.entity.master.Tenant;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.service.EmployeeService;
import com.example.sales_system.service.TenantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantController {
    TenantService tenantService;
    EmployeeService employeeService;
    DataSource tenantDataSource;


    @GetMapping("/create")
//    @Transactional(transactionManager = "tenantTransactionManager")
//    @Modifying
    public ResponseEntity<Boolean> createTenant(@RequestParam String tenantId) {
//        TenantContext.setTenantId(tenantId);

        // TODO: move to DatabaseService
        tenantService.createSchema(tenantId);

        Resource initSchemaScript = new ClassPathResource("scripts/schema.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchemaScript);
        try {
            Connection connection = tenantDataSource.getConnection();
            connection.setSchema(tenantId);
            databasePopulator.populate(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // DatabasePopulatorUtils.execute(databasePopulator, dataSource);

        // PSQLException: ERROR: schema "ttest1" already exists
        // PSQLException: ERROR: relation "employee" already exists
        // PSQLException: ERROR: no schema has been selected to create in

        // TransactionRequiredException: Executing an update/delete query

        return ResponseEntity.ok(true);
    }


    @PostMapping("/regis")
    @Transactional
    @Modifying
    public ResponseEntity<TenantCreateResponse> createTenantWithAdminUser(@RequestBody TenantCreateRequest request) {
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
                        .tenantId(tenant.getName())
                        .username(employee.getUsername())
                        .build()
        );
    }


}
