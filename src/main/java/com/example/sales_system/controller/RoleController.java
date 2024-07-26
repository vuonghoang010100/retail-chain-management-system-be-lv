package com.example.sales_system.controller;

import com.example.sales_system.dto.request.RoleCreateRequest;
import com.example.sales_system.dto.request.RoleUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.RoleResponse;
import com.example.sales_system.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @GetMapping
    public AppResponse<List<RoleResponse>> getAllRoles() {
        return AppResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @GetMapping("/{id}")
    public AppResponse<RoleResponse> getRoleById(@PathVariable Long id) {
        return AppResponse.<RoleResponse>builder()
                .result(roleService.getRoleResponse(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<RoleResponse> createRole(@RequestBody @Valid RoleCreateRequest request) {
        return AppResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @PutMapping("/{id}")
    public AppResponse<RoleResponse> updateRole(@RequestBody @Valid RoleUpdateRequest request, @PathVariable Long id) {
        return AppResponse.<RoleResponse>builder()
                .result(roleService.updateRole(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

}
