package com.example.sales_system.controller;

import com.example.sales_system.configuration.TenantContext;
import com.example.sales_system.dto.request.AuthenticationRequest;
import com.example.sales_system.dto.request.UserCreateResquest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.AuthenticationRespone;
import com.example.sales_system.dto.response.UserWithTenantResponse;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.service.AuthenticationService;
import com.example.sales_system.service.TenantService;
import com.example.sales_system.service.UserSevice;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    TenantService tenantService;
    UserSevice userSevice;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<UserWithTenantResponse> registerTenant(@RequestBody @Valid UserCreateResquest resquest) {
        return AppResponse.<UserWithTenantResponse>builder()
                .result(userSevice.createUser(resquest))
                .build();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<AuthenticationRespone> login(@RequestBody @Valid AuthenticationRequest resquest) {
        AuthenticationRespone authenticationRespone;
        // case admin app
        if (resquest.getTenant().equalsIgnoreCase("admin")) {
            // authenticate
            authenticationRespone = authenticationService.authenticateAdminApp(resquest);
        }
        // case tenant app
        else {
            var tenant = tenantService.getTenantByName(resquest.getTenant());
            if (!tenant.getActive())
                throw new AppException(AppStatusCode.TENANT_INACTIVE);
            // Set tenant context
            TenantContext.setTenantId(tenant.getName());
            // authenticate
            authenticationRespone = authenticationService.authenticateTenantApp(resquest);
        }

        return AppResponse.<AuthenticationRespone>builder()
                .result(authenticationRespone)
                .build();
    }

}
