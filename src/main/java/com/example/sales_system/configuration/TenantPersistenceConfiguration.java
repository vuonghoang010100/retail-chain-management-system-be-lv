package com.example.sales_system.configuration;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.MultiTenancySettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager",
        basePackages = {TenantPersistenceConfiguration.REPOSITORY_PACKAGE}
)
public class TenantPersistenceConfiguration {

    static final String PERSISTENCE_UNIT_NAME = "tenant";
    static final String ENTITY_PACKAGE = "com.example.sales_system.entity.tenant";
    static final String REPOSITORY_PACKAGE = "com.example.sales_system.repository.tenant";

    @Bean(name = "tenantDataSourceProperties")
    @ConfigurationProperties("spring.datasource.tenant")
    public DataSourceProperties tenantDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "tenantDataSource")
    @ConfigurationProperties("spring.datasource.tenant.configuration")
    public HikariDataSource tenantDataSource(
            @Qualifier("tenantDataSourceProperties") DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "tenantJpaProperties")
    @ConfigurationProperties("spring.jpa.tenant")
    public JpaProperties tenantJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            @Qualifier("tenantDataSource") DataSource tenantDataSource,
            @Qualifier("tenantJpaProperties") JpaProperties tenantJpaProperties,
            CurrentTenantIdentifierResolver<?> currentTenantIdentifierResolver,
            MultiTenantConnectionProvider<?> multiTenantConnectionProvider
    ) {
        // map JPA properties
        Map<String, Object> properties = new HashMap<>();
        properties.put(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        properties.put(MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        tenantJpaProperties.setOpenInView(false);
        properties.putAll(tenantJpaProperties.getProperties());

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        EntityManagerFactoryBuilder builder =
                new EntityManagerFactoryBuilder(jpaVendorAdapter, properties, null);

        return builder
                .dataSource(tenantDataSource)
                .packages(ENTITY_PACKAGE)
                .persistenceUnit(PERSISTENCE_UNIT_NAME)
                .build();
    }

    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManagerFactory
    ) {
        return new JpaTransactionManager(tenantEntityManagerFactory);
    }
}
