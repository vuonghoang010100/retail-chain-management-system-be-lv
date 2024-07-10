package com.example.sales_system.configuration;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "masterEntityManagerFactory",
        transactionManagerRef = "masterTransactionManager",
        basePackages = {MasterJpaConfiguration.REPOSITORY_PACKAGE})
public class MasterJpaConfiguration {

    static final String PERSISTENCE_UNIT_NAME = "master";
    static final String ENTITY_PACKAGE = "com.example.sales_system.entity.master";
    static final String REPOSITORY_PACKAGE = "com.example.sales_system.repository.master";


    @Primary
    @Bean(name = "masterDataSourceProperties")
    @ConfigurationProperties("spring.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.master.configuration")
    public DataSource masterDataSource(
            @Qualifier("masterDataSourceProperties") DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "masterJpaProperties")
    @ConfigurationProperties("spring.jpa.master")
    public JpaProperties masterJpaProperties() {
        return new JpaProperties();
    }

    @Primary
    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(
            @Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("masterJpaProperties") JpaProperties masterJpaProperties
    ) {
        // config default JPA properties
        masterJpaProperties.setOpenInView(false);

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        EntityManagerFactoryBuilder builder =
                new EntityManagerFactoryBuilder(jpaVendorAdapter, masterJpaProperties.getProperties(), null);

        return builder
                .dataSource(masterDataSource)
                .packages(ENTITY_PACKAGE)
                .persistenceUnit(PERSISTENCE_UNIT_NAME)
                .build();
    }

    @Primary
    @Bean(name = "masterTransactionManager")
    public PlatformTransactionManager masterTransactionManager(
            @Qualifier("masterEntityManagerFactory") EntityManagerFactory masterEntityManagerFactory
    ) {
        return new JpaTransactionManager(masterEntityManagerFactory);
    }
}
