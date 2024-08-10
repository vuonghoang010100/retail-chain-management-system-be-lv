package com.example.sales_system.controller;

import com.example.sales_system.dto.request.RoleCreateRequest;
import com.example.sales_system.dto.request.RoleUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.RoleResponse;
import com.example.sales_system.entity.tenant.Role;
import com.example.sales_system.service.RoleService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.util.SortBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Role", description = "The Role API.")
public class RoleController {
    RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('ROLE_READ')")
    public AppResponse<ListResponse<RoleResponse>> getAllRoles(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Role.class));

        // Search
        var spec = new FilterSpecificationBuilder<Role>()
                .or("name", FilterOperator.LIKE, search)
                .build();

        return AppResponse.<ListResponse<RoleResponse>>builder()
                .result(roleService.getAllRoles(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('ROLE_READ')")
    public AppResponse<RoleResponse> getRoleById(@PathVariable Long id) {
        return AppResponse.<RoleResponse>builder()
                .result(roleService.getRoleResponse(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('ROLE_CREATE')")
    public AppResponse<RoleResponse> createRole(@RequestBody @Valid RoleCreateRequest request) {
        return AppResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('ROLE_UPDATE')")
    public AppResponse<RoleResponse> updateRole(@RequestBody @Valid RoleUpdateRequest request, @PathVariable Long id) {
        return AppResponse.<RoleResponse>builder()
                .result(roleService.updateRole(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasAuthority('ROLE_DELETE')")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

}
