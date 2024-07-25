package com.example.sales_system.controller;


import com.example.sales_system.dto.request.TenantCreateRequest1;
import com.example.sales_system.dto.response.TenantCreateResponse1;
import com.example.sales_system.entity.master.Tenant;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.service.EmployeeService1;
import com.example.sales_system.service.TenantService1;
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
public class TenantController1 {
    TenantService1 tenantService;
    EmployeeService1 employeeService;
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
    public ResponseEntity<TenantCreateResponse1> createTenantWithAdminUser(@RequestBody TenantCreateRequest1 request) {
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
                TenantCreateResponse1.builder()
                        .tenantId(tenant.getName())
                        .username(employee.getEmail())
                        .build()
        );
    }


}
