package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.PermissionResponse;
import com.example.sales_system.entity.tenant.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);
}
