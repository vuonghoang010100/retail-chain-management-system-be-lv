package com.example.sales_system.dto.request;

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
public class ContractCreateRequest {
    @NotNull(message = "START_DATE_IS_REQUIRE")
    LocalDate startDate;

    @NotNull(message = "END_DATE_IS_REQUIRED")
    LocalDate endDate;

    @NotNull(message = "PERIOD_IS_REQUIRED")
    @Positive(message = "PERIOD_MUST_BE_POSITIVE")
    Integer period;
    String pdfUrl;
    String note;

    @NotNull(message = "VENDORID_ID_REQUIRED")
    Long vendorId;
}