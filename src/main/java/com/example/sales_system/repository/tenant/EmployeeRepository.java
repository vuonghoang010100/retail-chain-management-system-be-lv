package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);
}
