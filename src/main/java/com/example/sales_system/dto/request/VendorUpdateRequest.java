package com.example.sales_system.dto.request;

import com.example.sales_system.enums.VendorStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Vendor}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorUpdateRequest {
    @NotEmpty(message = "FULLNAME_IS_REQUIRED")
    String fullName;
    String email;
    String phone;
    String address;
    String province;
    String district;
    VendorStatus status;
    String note;
}