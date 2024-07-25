package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}