package com.example.sales_system.controller;

import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.PermissionResponse;
import com.example.sales_system.service.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Permission", description = "The Permission API.")
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAnyAuthority('ROLE_CREATE', 'ROLE_UPDATE')")
    public AppResponse<List<PermissionResponse>> getAllPermissions() {
        return AppResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermissions())
                .build();
    }
}
