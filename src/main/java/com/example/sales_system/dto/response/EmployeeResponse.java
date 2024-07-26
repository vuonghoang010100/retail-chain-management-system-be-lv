package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Employee}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponse {
    Long id;
    String fullName;
    LocalDate dob;
    String gender;
    String email;
    String phone;
    String address;
    String province;
    String district;
    Boolean active;
    String note;
    Set<RoleResponse> roles;
    String createTime;
    String updateTime;
}