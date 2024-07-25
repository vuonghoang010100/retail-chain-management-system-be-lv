package com.example.sales_system.service;

import com.example.sales_system.entity.tenant.Permission;
import com.example.sales_system.repository.tenant.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;

    public Permission create(String name, String description) {
        Permission permission = new Permission(name, description);
        return permissionRepository.save(permission);
    }
}
