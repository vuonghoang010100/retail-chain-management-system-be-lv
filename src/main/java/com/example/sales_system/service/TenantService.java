package com.example.sales_system.service;

import com.example.sales_system.dto.response.TenantResponse;
import com.example.sales_system.entity.master.Tenant;
import com.example.sales_system.entity.master.User;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.TenantMapper;
import com.example.sales_system.repository.master.TenantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantService {
    TenantRepository tenantRepository;
    TenantMapper tenantMapper;

    EmployeeService employeeService;
    RoleService roleService;
    PermissionService permissionService;

    @PersistenceContext(unitName = "tenant")
    EntityManager tenantEm;
    DataSource tenantDataSource;

    public List<TenantResponse> getAllTenants() {
        List<Tenant> tenants = tenantRepository.findAll();
        return tenants.stream().map(tenantMapper::toTenantResponse).toList();
    }

    public void active(Tenant tenant) {
        tenant.setInitStatus(true);
        tenant.setActive(true);
        tenantRepository.save(tenant);
    }

    public Tenant getTenant(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.TENANT_NOT_FOUND));
    }

    public Tenant getTenantByName(String name) {
        return tenantRepository.findByName(name)
                .orElseThrow(() -> new AppException(AppStatusCode.TENANT_NOT_FOUND));

    }

    /* Tenant database functions */

    @Transactional(transactionManager = "tenantTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void createSchema(String name) {
        tenantEm.createNativeQuery("CREATE SCHEMA t_%s".formatted(name)).executeUpdate();
    }

    @Transactional(transactionManager = "tenantTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void initTenantTables(Tenant tenant) {
        Resource initSchemaScript = new ClassPathResource("scripts/schema.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchemaScript);
        Connection connection;
        try {
            connection = tenantDataSource.getConnection();
            connection.setSchema("t_" + tenant.getName());
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            databasePopulator.populate(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(transactionManager = "tenantTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void initTenantDatas(Tenant tenant) {
        permissionService.generatePermission();
        var role = roleService.createAdminRole();

        User user = tenant.getUser();
        Employee employee = Employee.builder()
                .fullName(user.getFullName())
                .dob(user.getDob())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .password(user.getPassword())
                .address(user.getAddress())
                .province(user.getProvince())
                .district(user.getDistrict())
                .active(true)
                .roles(new HashSet<>(Collections.singleton(role)))
                .build();

        employeeService.saveEmployee(employee);
    }
}
