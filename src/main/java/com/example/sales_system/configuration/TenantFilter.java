package com.example.sales_system.configuration;

import com.example.sales_system.service.LookupService;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
@Slf4j
public class TenantFilter extends OncePerRequestFilter {
    private final LookupService lookupService;

    public TenantFilter(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);

            try {
                SignedJWT signedJWT = SignedJWT.parse(bearerToken);
                Object tenant = signedJWT.getJWTClaimsSet().getClaim("tenant");
//                TenantContext.setTenantId(tenant.toString());

                String tenantName = tenant.toString();
                if (! tenantName.equals("admin")) {
                    tenantName = lookupService.getSchema(tenantName);
                }
                TenantContext.setTenantId(tenantName);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
