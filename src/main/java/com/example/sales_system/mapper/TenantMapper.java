package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.TenantResponse;
import com.example.sales_system.entity.master.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantMapper {
    TenantResponse toTenantResponse(Tenant tenant);

//    TenantWithoutUserResponse toTenantWithoutUserResponse(Tenant tenant);
}
