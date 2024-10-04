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
 * DTO for {@link com.example.sales_system.entity.tenant.PurchaseDetail}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseDetailReceiveRequest {
    Long id;

    @NotNull(message = "PURCHASE_RECEIVE_AMOUNT_IS_REQUIRED")
    @Positive(message = "PURCHASE_RECEIVE_AMOUNT_MUST_BE_POSITIVE")
    Long receivedAmount;

    LocalDate mfg;

    LocalDate exp;
}
