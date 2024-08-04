package com.example.sales_system.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Role}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequest {
    String name;
    String description;
    List<String> permissions;
}