package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Inventory}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResponse {
    Long id;

    StoreResponse store;
    EmployeeResponseSimple employee;
    Set<InventoryDetailResponse> details;

    String createTime;
    String updateTime;
}