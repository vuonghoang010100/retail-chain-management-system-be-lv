package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Role}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Long id;
    String name;
    String description;
    Set<PermissionResponse> permissions;
    String createTime;
    String updateTime;
}