package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    List<Employee> findAllByAllStore(Boolean allStore);
}
