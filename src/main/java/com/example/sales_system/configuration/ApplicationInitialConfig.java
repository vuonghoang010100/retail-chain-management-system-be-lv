package com.example.sales_system.configuration;

import com.example.sales_system.entity.master.Role;
import com.example.sales_system.entity.master.Tenant;
import com.example.sales_system.entity.master.User;
import com.example.sales_system.enums.AppRole;
import com.example.sales_system.repository.master.MasterRoleRepository;
import com.example.sales_system.repository.master.TenantRepository;
import com.example.sales_system.repository.master.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class ApplicationInitialConfig {

    private final TenantRepository tenantRepository;
    private final MasterRoleRepository masterRoleRepository;
    private final UserRepository userRepository;
    @Value("${master.admin.email}")
    private String adminEmail;
    @Value("${master.admin.password}")
    private String adminPassword;

    public ApplicationInitialConfig(TenantRepository tenantRepository, MasterRoleRepository masterRoleRepository, UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.masterRoleRepository = masterRoleRepository;
        this.userRepository = userRepository;
    }

    @Bean
    @Profile("local")
    ApplicationRunner applicationRunner() {
        return args -> {
            // Init Master Database
            //// Create default roles
            if (!masterRoleRepository.existsById(AppRole.SYSTEM_ADMIN.name())) {
                Role master_admin = new Role(
                        AppRole.SYSTEM_ADMIN.name(),
                        "Master admin role");
                Role tenant_admin = new Role(
                        AppRole.TENANT_ADMIN.name(),
                        "Tenant admin role");
                masterRoleRepository.save(master_admin);
                masterRoleRepository.save(tenant_admin);
            }
            //// Create system admin
            if (!userRepository.existsByEmail(adminEmail)) {
                Role role = masterRoleRepository.findById(AppRole.SYSTEM_ADMIN.name()).get();
                Set<Role> adminRoles = new HashSet<Role>();
                adminRoles.add(role);
                User user = User.builder()
                        .fullName("admin")
                        .email(adminEmail)
                        .password(adminPassword)
                        .roles(adminRoles)
                        .build();
                userRepository.save(user);
            }

            //// Create default tenant
            final String DEFAULT_TENANT = CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID;
            if (!tenantRepository.existsByName(DEFAULT_TENANT)) {
                Role role = masterRoleRepository.findById(AppRole.TENANT_ADMIN.name()).get();
                Set<Role> userRoles = new HashSet<Role>();
                userRoles.add(role);

                User user = User.builder()
                        .fullName("test")
                        .email("test@test.com")
                        .password("test")
                        .roles(userRoles)
                        .build();
                userRepository.save(user);

                Tenant tenant = Tenant.builder().name(DEFAULT_TENANT)
                        .initStatus(false)
                        .active(true)
                        .user(user)
                        .build();
                tenantRepository.save(tenant);
            }

            // Init Tenant Database

            // More actions ...

        };
    }
}
