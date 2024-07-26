package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.master.Tenant}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantWithoutUserResponse {
    Long id;
    String name;
    String companyName;
    String address;
    String province;
    String district;
    String taxId;
    String BusinessLicenseUrl;
    Boolean initStatus;
    Boolean active;
    String createTime;
    String updateTime;
}