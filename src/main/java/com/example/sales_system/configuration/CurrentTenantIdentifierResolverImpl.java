package com.example.sales_system.configuration;


import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {

    public static final String DEFAULT_TENANT_ID = "test";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();
        return StringUtils.hasText(tenantId) ? tenantId : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
