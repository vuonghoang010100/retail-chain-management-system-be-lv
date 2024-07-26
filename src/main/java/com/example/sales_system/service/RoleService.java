package com.example.sales_system.service;

import com.example.sales_system.dto.request.RoleCreateRequest;
import com.example.sales_system.dto.request.RoleUpdateRequest;
import com.example.sales_system.dto.response.RoleResponse;
import com.example.sales_system.entity.tenant.Permission;
import com.example.sales_system.entity.tenant.Role;
import com.example.sales_system.enums.AppRole;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.RoleMapper;
import com.example.sales_system.repository.tenant.PermissionRepository;
import com.example.sales_system.repository.tenant.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public RoleResponse getRoleResponse(Long id) {
        return roleMapper.toRoleResponse(getRoleById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public RoleResponse createRole(RoleCreateRequest request) {
        Role role = roleMapper.toRole(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public RoleResponse updateRole(RoleUpdateRequest request, Long id) {
        Role role = getRoleById(id);
        roleMapper.updateRole(role, request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    /* Helper functions */

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.ROLE_NOT_FOUND));
    }

    /* System admin functions */

    public Role createAdminRole() {
        Role role = Role.builder()
                .name(AppRole.TENANT_ADMIN.name())
                .description("Admin")
                .build();
        return roleRepository.save(role);
    }
}
