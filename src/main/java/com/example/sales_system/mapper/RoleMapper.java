package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.RoleCreateRequest;
import com.example.sales_system.dto.request.RoleUpdateRequest;
import com.example.sales_system.dto.response.RoleResponse;
import com.example.sales_system.entity.tenant.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreateRequest roleCreateRequest);

    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest roleUpdateRequest);
}
