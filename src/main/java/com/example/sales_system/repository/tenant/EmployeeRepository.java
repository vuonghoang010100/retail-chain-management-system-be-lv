package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);
}
