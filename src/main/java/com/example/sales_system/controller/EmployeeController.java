package com.example.sales_system.controller;


import com.example.sales_system.configuration.multitenant.TenantIdentifierResolver;
import com.example.sales_system.dto.request.EmployeeCreateRequest;
import com.example.sales_system.entity.Employee;
import com.example.sales_system.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {
    TenantIdentifierResolver tenantIdentifierResolver;
    EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees(@RequestParam String tenantId) {
        tenantIdentifierResolver.setCurrentTenant(tenantId);

        return employeeService.getAllEmployees();
    }

    @PostMapping
    public Employee createEmployee(@RequestParam String tenantId, @RequestBody EmployeeCreateRequest request) {
        tenantIdentifierResolver.setCurrentTenant(tenantId);

        return employeeService.createEmployee(request);
    }

}
