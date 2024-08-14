package com.example.sales_system.dto.request;

import com.example.sales_system.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Customer}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCreateRequest {
    @NotEmpty(message = "FULLNAME_IS_REQUIRED")
    String fullName;
    LocalDate dob;
    Gender gender;

    @Email(message = "INVALID_EMAIL")
    String email;

    @NotEmpty(message = "PHONE_IS_REQUIRED")
    String phone;

    String address;
    String province;
    String district;

    @Min(value = 0L, message = "REWARD_POINT_MUST_BE_GREATER_OR_EQUAL_THAN_ZERO")
    @Builder.Default
    Long rewardPoint = 0L;
    String note;
}