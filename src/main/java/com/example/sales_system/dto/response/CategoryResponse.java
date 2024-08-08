package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Category}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    Long id;
    String name;
    String description;
    String createTime;
    String updateTime;
}