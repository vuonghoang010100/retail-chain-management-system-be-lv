package com.example.sales_system.service;

import com.example.sales_system.entity.master.Tenant;
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
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantService1 {
    TenantRepository tenantRepository;
    @PersistenceContext(unitName = "tenant")
    EntityManager entityManager;
    DataSource dataSource;

    public Tenant createTenantAndSchema(String tenantId) {
        Tenant tenant = this.createTenant(tenantId);
        this.createSchemaForTenant(tenantId);
        return tenant;
    }

    public Tenant createTenant(String tenantId) {
//        if (tenantRepository.existsByName(tenantId))
//            // TODO: New exception class - enums
//            throw new RuntimeException("TENANT_EXISTED");

        Tenant tenant = Tenant.builder()
                .name(tenantId)
                .initStatus(false)
                .build();

        return tenantRepository.save(tenant);
    }

    public void createSchemaForTenant(String tenantId) {
        // Init schema
        entityManager.createNativeQuery("CREATE SCHEMA t_%s".formatted(tenantId)).executeUpdate();
        // change schema
        entityManager.createNativeQuery("SET SCHEMA 't_%s'".formatted(tenantId)).executeUpdate();

        // Init schema tables
        Resource initSchemaScript = new ClassPathResource("scripts/schema.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchemaScript);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public int createSchema(String tenantId) {
        return entityManager.createNativeQuery("CREATE SCHEMA t_%s".formatted(tenantId)).executeUpdate();
    }

}
