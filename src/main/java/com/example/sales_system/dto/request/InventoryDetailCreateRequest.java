package com.example.sales_system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.InventoryDetail}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryDetailCreateRequest {
    @NotNull(message = "BATCH_ID_IS_REQUIRED")
    Long batchId;

    @NotNull(message = "REAL_QUANTITY_IS_REQUIRED")
    Long realQuantity;
}