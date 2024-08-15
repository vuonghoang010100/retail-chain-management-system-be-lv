package com.example.sales_system.dto.request;

import com.example.sales_system.enums.ContractStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Contract}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractUpdateRequest {
    @NotNull(message = "START_DATE_IS_REQUIRE")
    LocalDate startDate;

    @NotNull(message = "END_DATE_IS_REQUIRED")
    LocalDate endDate;

    @NotNull(message = "PERIOD_IS_REQUIRED")
    @Positive(message = "PERIOD_MUST_BE_POSITIVE")
    Integer period;
    String pdfUrl;
    ContractStatus status;
    String note;

    @NotNull(message = "VENDORID_ID_REQUIRED")
    Long vendorId;
}