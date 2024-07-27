package com.example.sales_system.configuration;

import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.exception.AppStatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AppStatusCode statusCode = AppStatusCode.UNAUTHENTICATED;

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        AppResponse<?> appResponse = AppResponse.builder()
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), appResponse);
        response.flushBuffer();
    }
}
