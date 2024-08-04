package com.example.sales_system.repository.master;


import com.example.sales_system.entity.master.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    boolean existsByName(String name);

    Optional<Tenant> findByName(String name);
}
