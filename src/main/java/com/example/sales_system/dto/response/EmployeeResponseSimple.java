package com.example.sales_system.dto.response;

import com.example.sales_system.enums.EmployeeStatus;
import com.example.sales_system.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Employee}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponseSimple {
    Long id;
    String fullName;
    LocalDate dob;
    Gender gender;
    String email;
    String phone;
    String address;
    String province;
    String district;
    EmployeeStatus status;
    String note;
    String createTime;
    String updateTime;
}
