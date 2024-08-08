package com.example.sales_system.dto.response;

import com.example.sales_system.enums.StoreStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Store}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreResponse {
    Long id;
    String name;
    String fullName;
    String province;
    String district;
    String address;
    String email;
    String phone;
    StoreStatus status;
    String note;
    String createTime;
    String updateTime;
}