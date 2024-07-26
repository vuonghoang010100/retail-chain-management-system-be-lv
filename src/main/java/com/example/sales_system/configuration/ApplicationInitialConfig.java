package com.example.sales_system.configuration;

import com.example.sales_system.entity.master.Role;
import com.example.sales_system.entity.master.Tenant;
import com.example.sales_system.entity.master.User;
import com.example.sales_system.enums.AppRole;
import com.example.sales_system.repository.master.MasterRoleRepository;
import com.example.sales_system.repository.master.TenantRepository;
import com.example.sales_system.repository.master.UserRepository;
import com.example.sales_system.repository.tenant.EmployeeRepository;
import com.example.sales_system.repository.tenant.PermissionRepository;
import com.example.sales_system.repository.tenant.RoleRepository;
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
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Value("${master.admin.email}")
    private String adminEmail;
    @Value("${master.admin.password}")
    private String adminPassword;

    public ApplicationInitialConfig(
            TenantRepository tenantRepository,
            MasterRoleRepository masterRoleRepository,
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        this.tenantRepository = tenantRepository;
        this.masterRoleRepository = masterRoleRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Bean
    @Profile("local")
    ApplicationRunner applicationRunner() {
        return args -> {
            // Init Master Database
            if (!masterRoleRepository.existsById(AppRole.SYSTEM_ADMIN.name())) {
                // create roles
                Role master_admin = new Role(
                        AppRole.SYSTEM_ADMIN.name(),
                        "Master admin role");
                Role tenant_admin = new Role(
                        AppRole.TENANT_ADMIN.name(),
                        "Tenant admin role");
                masterRoleRepository.save(master_admin);
                masterRoleRepository.save(tenant_admin);

                // Create system admin
                if (!userRepository.existsByEmail(adminEmail)) {
                    Set<Role> adminRoles = new HashSet<>();
                    adminRoles.add(master_admin);
                    User user = User.builder()
                            .fullName("admin")
                            .email(adminEmail)
                            .password(adminPassword)
                            .roles(adminRoles)
                            .build();
                    userRepository.save(user);
                }

                // Create default tenant
                final String DEFAULT_TENANT = CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID;
                if (!tenantRepository.existsByName(DEFAULT_TENANT)) {
                    Set<Role> userRoles = new HashSet<>();
                    userRoles.add(tenant_admin);

                    User user = User.builder()
                            .fullName("test")
                            .email("test@test.com")
                            .password("test")
                            .roles(userRoles)
                            .build();
                    userRepository.save(user);

                    Tenant tenant = Tenant.builder().name(DEFAULT_TENANT)
                            .initStatus(false)
                            .active(false)
                            .initStatus(false)
                            .user(user)
                            .build();
                    tenantRepository.save(tenant);
                }
            }

            // Init Default Tenant Database
//            if (!employeeRepository.existsByEmail("test@test.com")) {
//                // Create default permissions
//                Arrays.stream(TenantPermission.values()).forEach(permission -> permissionRepository.save(new Permission(permission.getName(), permission.getDescription())));
//                // Create tenant admin role
//                var role = roleRepository.save(com.example.sales_system.entity.tenant.Role.builder()
//                        .name(AppRole.TENANT_ADMIN.name())
//                        .build());
//                // Create tenant admin
//
//                Employee employee = Employee.builder()
//                        .fullName("test")
//                        .email("test@test.com")
//                        .password("test")
//                        .roles(new HashSet<>(List.of(role)))
//                        .active(true)
//                        .build();
//
//                employeeRepository.save(employee);
//            }

            // More actions ...

        };
    }
}
