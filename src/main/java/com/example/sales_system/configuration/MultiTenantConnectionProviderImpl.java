package com.example.sales_system.configuration;

import io.micrometer.common.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider<String> {

    private final DataSource tenantDataSource;

    public MultiTenantConnectionProviderImpl(@Qualifier("tenantDataSource") DataSource tenantDataSource) {
        this.tenantDataSource = tenantDataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection(CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String schema) throws SQLException {
        log.info("Getting connection for schema {}", schema);
        final Connection connection = tenantDataSource.getConnection();
        connection.setSchema(schema);
        return connection;
    }

    @Override
    public void releaseConnection(String schema, Connection connection) throws SQLException {
        log.info("Release connection for schema {}", schema);
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(@NonNull Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> aClass) {
        throw new UnsupportedOperationException("Can't unwrap this.");
    }
}
