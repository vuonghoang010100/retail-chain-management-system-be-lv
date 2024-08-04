package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.master.User}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserWithTenantResponse {
    Long id;
    String fullName;
    LocalDate dob;
    String gender;
    String email;
    String phone;
    String address;
    String province;
    String district;
    String createTime;
    String updateTime;
    TenantWithoutUserResponse tenant;
}