package com.example.sales_system.controller;

import com.example.sales_system.dto.request.EmployeeCreateRequest;
import com.example.sales_system.dto.request.EmployeeUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.EmployeeResponse;
import com.example.sales_system.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmployeeController {
    EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<List<EmployeeResponse>> getAllEmployees() {
        log.debug("getAllEmployees called");
        return AppResponse.<List<EmployeeResponse>>builder()
                .result(employeeService.getAllEmployeeResponses())
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        log.debug("getEmployeeById called");
        return AppResponse.<EmployeeResponse>builder()
                .result(employeeService.getEmployeeResponseById(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeCreateRequest request) {
        log.debug("createEmployee called");
        return AppResponse.<EmployeeResponse>builder()
                .result(employeeService.createEmployee(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody @Valid EmployeeUpdateRequest request) {
        log.debug("updateEmployee called");
        return AppResponse.<EmployeeResponse>builder()
                .result(employeeService.updateEmployee(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        log.debug("deleteEmployee called");
        employeeService.deleteEmployeeById(id);
    }

}
