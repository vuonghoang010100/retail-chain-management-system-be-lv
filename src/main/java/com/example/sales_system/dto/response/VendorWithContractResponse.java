package com.example.sales_system.dto.response;

import com.example.sales_system.enums.VendorStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Vendor}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorWithContractResponse {
    Long id;
    String fullName;
    String email;
    String phone;
    String address;
    String province;
    String district;
    VendorStatus status;
    String note;

    Set<ContractWoVendorResponse> contracts;

    String createTime;
    String updateTime;
}