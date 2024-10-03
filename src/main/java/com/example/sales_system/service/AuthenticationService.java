package com.example.sales_system.service;

import com.example.sales_system.dto.request.AuthenticationRequest;
import com.example.sales_system.dto.response.AuthenticationRespone;
import com.example.sales_system.entity.master.Role;
import com.example.sales_system.entity.master.User;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.entity.tenant.Permission;
import com.example.sales_system.enums.AppRole;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.repository.master.UserRepository;
import com.example.sales_system.repository.tenant.EmployeeRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    EmployeeRepository employeeRepository;

    PasswordEncoder passwordEncoder;
    MACSigner macSigner;

    public AuthenticationRespone authenticateAdminApp(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(AppStatusCode.EMAIL_PASSWORD_INCORRECT));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(AppStatusCode.EMAIL_PASSWORD_INCORRECT);
        }

        var token = generateToken(user);
        return AuthenticationRespone.builder()
                .token(token)
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public AuthenticationRespone authenticateTenantApp(AuthenticationRequest request) {
        var employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(AppStatusCode.EMAIL_PASSWORD_INCORRECT));

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new AppException(AppStatusCode.EMAIL_PASSWORD_INCORRECT);
        }

        var token = generateToken(employee, request.getTenant());
        return AuthenticationRespone.builder()
                .token(token)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(12, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("tenant", "admin")
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(macSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateToken(Employee employee, String tenant) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(employee.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(12, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("tenant", tenant)
                .claim("scope", buildScope(employee))
                .claim("userId", employee.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(macSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return stringJoiner.toString();
        }

        for (Role role : user.getRoles()) {
            stringJoiner.add("ROLE_" + role.getName());
        }

        return stringJoiner.toString();
    }

    private String buildScope(Employee employee) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (CollectionUtils.isEmpty(employee.getRoles())) {
            return stringJoiner.toString();
        }
        employee.getRoles().forEach(role -> {
            if (role.getName().equals(AppRole.ADMIN.name())) {
                stringJoiner.add("ROLE_" + role.getName());
            }
            for (Permission permission : role.getPermissions()) {
                stringJoiner.add(permission.getName());
            }
        });
        return stringJoiner.toString();
    }



}
