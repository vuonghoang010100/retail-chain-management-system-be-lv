package com.example.sales_system.service;


import com.example.sales_system.dto.request.EmployeeCreateRequest1;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.repository.tenant.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmployeeService1 {
    EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createAdminEmployee(String username, String password) {
        Employee employee = Employee.builder()
                .email(username)
                .password(password)
                .build();
        return employeeRepository.save(employee);
    }

    public Employee createEmployee(EmployeeCreateRequest1 request) {
        Employee employee = Employee.builder()
                .email(request.getUsername())
                .password(request.getPassword())
                .build();
        return employeeRepository.save(employee);
    }
}
