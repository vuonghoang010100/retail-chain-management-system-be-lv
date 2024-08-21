package com.example.sales_system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.PurchaseDetail}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseDetailCreateRequest {
    @NotNull(message = "PRODUCT_ID_IS_REQUIRED")
    Long productId;

    @NotNull(message = "PURCHASE_PRICE_IS_REQUIRED")
    @Positive(message = "PURCHASE_PRICE_MUST_BE_POSITIVE")
    Long purchasePrice;

    @NotNull(message = "PURCHASE_AMOUNT_IS_REQUIRED")
    @Positive(message = "PURCHASE_AMOUNT_MUST_BE_POSITIVE")
    Long purchaseAmount;
}