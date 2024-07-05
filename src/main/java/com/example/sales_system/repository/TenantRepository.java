package com.example.sales_system.repository;


import com.example.sales_system.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    boolean existsByTenantId(String tenantId);
}
