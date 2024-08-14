package com.example.sales_system.dto.response;

import com.example.sales_system.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Customer}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    Long id;
    String fullName;
    LocalDate dob;
    Gender gender;
    String email;
    String phone;
    String address;
    String province;
    String district;
    Long rewardPoint;
    String note;

    String createTime;
    String updateTime;
}