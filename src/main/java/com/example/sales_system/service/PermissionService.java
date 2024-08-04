package com.example.sales_system.service;

import com.example.sales_system.dto.response.PermissionResponse;
import com.example.sales_system.entity.tenant.Permission;
import com.example.sales_system.enums.TenantPermission;
import com.example.sales_system.mapper.PermissionMapper;
import com.example.sales_system.repository.tenant.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public Permission create(String name, String description) {
        Permission permission = new Permission(name, description);
        return permissionRepository.save(permission);
    }

    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void generatePermission() {
        Arrays.stream(TenantPermission.values())
                .forEach(permission ->
                        permissionRepository.save(new Permission(permission.getName(), permission.getDescription()))
                );
    }
}
