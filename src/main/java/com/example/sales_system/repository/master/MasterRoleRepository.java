package com.example.sales_system.repository.master;

import com.example.sales_system.entity.master.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterRoleRepository extends JpaRepository<Role, String> {
}
