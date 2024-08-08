package com.example.sales_system.dto.request;

import com.example.sales_system.enums.EmployeeStatus;
import com.example.sales_system.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Employee}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeUpdateRequest {
    @NotEmpty(message = "FULLNAME_IS_REQUIRED")
    String fullName;
    LocalDate dob;
    Gender gender;

    @NotEmpty(message = "EMAIL_IS_REQUIRED")
    @Email(message = "INVALID_EMAIL")
    String email;
    String phone;
    String address;
    String province;
    String district;
    EmployeeStatus status;
    String note;
    List<Long> roles;

    Boolean allStore;
}