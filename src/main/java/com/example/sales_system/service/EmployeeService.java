package com.example.sales_system.service;

import com.example.sales_system.dto.request.EmployeeCreateRequest;
import com.example.sales_system.dto.request.EmployeeUpdateRequest;
import com.example.sales_system.dto.response.EmployeeResponse;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.EmployeeMapper;
import com.example.sales_system.repository.tenant.EmployeeRepository;
import com.example.sales_system.repository.tenant.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmployeeService {
    EmployeeRepository employeeRepository;
    RoleRepository roleRepository;
    EmployeeMapper employeeMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public List<EmployeeResponse> getAllEmployeeResponses() {
        log.debug("getAllEmployees called");
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toEmployeeResponse)
                .toList();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public EmployeeResponse getEmployeeResponseById(Long id) {
        log.debug("getEmployeeResponseById called");
        return employeeMapper.toEmployeeResponse(getEmployeeById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        log.debug("createEmployee called");
        Employee employee = employeeMapper.toEmployee(request);
        // update roles
        var roles = roleRepository.findAllById(request.getRoles());
        employee.setRoles(new HashSet<>(roles));
        // update active
        employee.setActive(true);
        // save
        employee = saveEmployee(employee);

        return employeeMapper.toEmployeeResponse(employee);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        var employee = this.getEmployeeById(id);
        employeeMapper.updateEmployee(employee, request);
        // update roles
        var roles = roleRepository.findAllById(request.getRoles());
        employee.setRoles(new HashSet<>(roles));
        employee = saveEmployee(employee);
        return employeeMapper.toEmployeeResponse(employee);
    }


    @Transactional(transactionManager = "tenantTransactionManager")
    public void deleteEmployeeById(Long id) {
        log.debug("deleteEmployeeById called");
        var employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    // ----- Helper functions -----

    private Employee getEmployeeById(Long id) {
        log.debug("getEmployeeById called");
        return employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.EMPLOYEE_NOT_FOUND));
    }

    public Employee saveEmployee(Employee employee) {
        try {
            employee = employeeRepository.save(employee);
        } catch (DataIntegrityViolationException exception) {
            log.debug(exception.getMessage());
            if (StringUtils.containsIgnoreCase(
                    exception.getMessage(),
                    String.format("Key (email)=(%s) already exists.", employee.getEmail()))) {
                throw new AppException(AppStatusCode.EMAIL_ALREADY_EXISTED);
            } else if (StringUtils.containsIgnoreCase(
                    exception.getMessage(),
                    String.format("Key (phone)=(%s) already exists.", employee.getPhone()))) {
                throw new AppException(AppStatusCode.PHONE_ALREADY_EXISTED);
            }
            throw exception;
        }
        return employee;
    }
}
