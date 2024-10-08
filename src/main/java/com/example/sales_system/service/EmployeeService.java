package com.example.sales_system.service;

import com.example.sales_system.dto.request.EmployeeCreateRequest;
import com.example.sales_system.dto.request.EmployeeUpdateRequest;
import com.example.sales_system.dto.response.EmployeeResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.enums.EmployeeStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.EmployeeMapper;
import com.example.sales_system.repository.tenant.EmployeeRepository;
import com.example.sales_system.repository.tenant.RoleRepository;
import com.example.sales_system.repository.tenant.StoreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmployeeService {
    EmployeeRepository employeeRepository;
    RoleRepository roleRepository;
    StoreRepository storeRepository;
    EmployeeMapper employeeMapper;
    PasswordEncoder passwordEncoder;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<EmployeeResponse> getAllEmployeeResponses(Specification<Employee> spec, Pageable pageable) {
        log.debug("getAllEmployees called with pageable {}", pageable);

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        return ListResponse.<EmployeeResponse>builder()
                .size(employeePage.getSize())
                .page(employeePage.getNumber() + 1)
                .total(employeePage.getTotalElements())
                .numOfElements(employeePage.getNumberOfElements())
                .totalPages(employeePage.getTotalPages())
                .data(employeePage.getContent().stream().map(employeeMapper::toEmployeeResponse).collect(Collectors.toList()))
                .build();
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
        // udpate password
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        // update roles
        employee.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));
        // update store
        if (employee.getAllStore()) {
            employee.setStores(new HashSet<>(storeRepository.findAll()));
        } else {
            employee.setStores(new HashSet<>(storeRepository.findAllById(request.getStores())));
        }
        // update active
        employee.setStatus(EmployeeStatus.ACTIVE);
        // save
        employee = saveEmployee(employee);

        return employeeMapper.toEmployeeResponse(employee);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        var employee = this.getEmployeeById(id);
        employeeMapper.updateEmployee(employee, request);
        // update roles
        employee.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));
        // update stores
        if (employee.getAllStore()) {
            employee.setStores(new HashSet<>(storeRepository.findAll()));
        } else {
            employee.setStores(new HashSet<>(storeRepository.findAllById(request.getStores())));
        }
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

    public Employee getEmployeeById(Long id) {
        log.debug("getEmployeeById called");
        return employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.EMPLOYEE_NOT_FOUND));
    }

    public Employee saveEmployee(Employee employee) {
        try {
            employee = employeeRepository.save(employee);
        } catch (DataIntegrityViolationException exception) {
            if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (email)")) {
                throw new AppException(AppStatusCode.EMAIL_ALREADY_EXISTED);
            } else if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (phone)")) {
                throw new AppException(AppStatusCode.PHONE_ALREADY_EXISTED);
            }
            log.debug(exception.getMessage());
            throw exception;
        }
        return employee;
    }

    public Employee getMyinfo() {
        log.debug("getMyinfo called");
        var myId = (Long) ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaims().get("userId");

        return getEmployeeById(myId);
    }
}
