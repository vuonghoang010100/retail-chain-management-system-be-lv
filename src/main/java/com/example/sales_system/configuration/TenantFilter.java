package com.example.sales_system.configuration;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
@Slf4j
public class TenantFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;
    private final MACVerifier macVerifier;

    public TenantFilter(@Qualifier("jwtDecoder") JwtDecoder jwtDecoder, MACVerifier macVerifier) {
        this.jwtDecoder = jwtDecoder;
        this.macVerifier = macVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);

            try {
                SignedJWT signedJWT = SignedJWT.parse(bearerToken);
                Object tenant = signedJWT.getJWTClaimsSet().getClaim("tenant");
                TenantContext.setTenantId(tenant.toString());

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
//            var decode = jwtDecoder.decode(bearerToken);
//            var tenant = decode.getClaims().get("tenant");
//            if (tenant != null) {
//                TenantContext.setTenantId((String)tenant);
//            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
//            log.debug("TenantContext clear");
        }
    }
}
