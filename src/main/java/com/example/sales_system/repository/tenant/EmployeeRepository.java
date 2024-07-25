package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Boolean existsByEmail(String email);
}
