package com.example.sales_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Store}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreCreateRequest {
    @NotEmpty(message = "NAME_IS_REQUIRED")
    String name;

    @NotEmpty(message = "FULLNAME_IS_REQUIRED")
    String fullName;

    @NotEmpty(message = "PROVINCE_IS_REQUIRED")
    String province;

    @NotEmpty(message = "DISTRICT_IS_REQUIRED")
    String district;

    @NotEmpty(message = "ADDRESS_IS_REQUIRED")
    String address;

    @Email(message = "INVALID_EMAIL")
    String email;
    String phone;

    String note;
}