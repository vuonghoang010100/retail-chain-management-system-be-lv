package com.example.sales_system.service;


import com.example.sales_system.entity.Employee;
import com.example.sales_system.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmployeeService {
    EmployeeRepository employeeRepository;

    public Employee createAdminEmployee(String username, String password) {
        Employee employee = Employee.builder()
                .username(username)
                .password(password)
                .build();
        return employeeRepository.save(employee);
    }

}
