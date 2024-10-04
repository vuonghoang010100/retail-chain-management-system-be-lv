package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.PurchaseDetail}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseDetailResponse {
    Long id;
    Long purchasePrice;
    Long purchaseAmount;
    Long receivedAmount;
    Long subTotal;
    LocalDate mfg;
    LocalDate exp;

    ProductResponse product;
    BatchResponseSimple batch;
}