package com.example.sales_system.dto.request;

import com.example.sales_system.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Bill}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillUpdateRequest {
    @NotNull(message = "PAYMENT_STATUS_IS_REQUIRED")
    PaymentStatus paymentStatus;
    String note;
}