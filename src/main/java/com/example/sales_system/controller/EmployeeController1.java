package com.example.sales_system.controller;


import com.example.sales_system.configuration.TenantContext;
import com.example.sales_system.dto.request.EmployeeCreateRequest1;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.service.EmployeeService1;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController1 {
    EmployeeService1 employeeService;


    @GetMapping
    public List<Employee> getAllEmployees(@RequestParam String tenantId) {
        TenantContext.setTenantId(tenantId);
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public Employee createEmployee(@RequestParam String tenantId, @RequestBody EmployeeCreateRequest1 request) {
        TenantContext.setTenantId(tenantId);
        return employeeService.createEmployee(request);
    }

}
