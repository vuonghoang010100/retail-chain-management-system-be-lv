package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.UserCreateResquest;
import com.example.sales_system.dto.response.UserWithTenantResponse;
import com.example.sales_system.entity.master.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = DateMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    User toUser(UserCreateResquest userCreateResquest);

    UserWithTenantResponse toUserWithTenantResponse(User user);
}
