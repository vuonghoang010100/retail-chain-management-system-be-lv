package com.example.sales_system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.TransferDetail}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferDetailCreationRequest {
    @NotNull(message = "BATCH_ID_IS_REQUIRED")
    Long batchId;

    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Long quantity;
}