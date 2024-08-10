package com.example.sales_system.service;

import com.example.sales_system.dto.request.UserCreateResquest;
import com.example.sales_system.dto.response.UserWithTenantResponse;
import com.example.sales_system.entity.master.Tenant;
import com.example.sales_system.entity.master.User;
import com.example.sales_system.enums.AppRole;
import com.example.sales_system.mapper.UserMapper;
import com.example.sales_system.repository.master.MasterRoleRepository;
import com.example.sales_system.repository.master.TenantRepository;
import com.example.sales_system.repository.master.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserSevice {
    UserMapper userMapper;
    UserRepository userRepository;
    MasterRoleRepository masterRoleRepository;
    TenantRepository tenantRepository;

    PasswordEncoder passwordEncoder;

    public UserWithTenantResponse createUser(UserCreateResquest resquest) {
        User user = userMapper.toUser(resquest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var role = masterRoleRepository.findById(AppRole.ADMIN.name());
        if (role.isPresent()) {
            user.setRoles(new HashSet<>(Collections.singleton(role.get())));
        }
        user = userRepository.save(user);

        Tenant tenant = Tenant.builder()
                .name(resquest.getTenant())
                .user(user)
                .initStatus(false)
                .active(false)
                .build();

        tenant = tenantRepository.save(tenant);
        user.setTenant(tenant);

        return userMapper.toUserWithTenantResponse(user);
    }
}
