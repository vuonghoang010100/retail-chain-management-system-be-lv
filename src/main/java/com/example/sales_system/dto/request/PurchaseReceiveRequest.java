package com.example.sales_system.dto.request;

import com.example.sales_system.enums.ReceiveStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Purchase}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseReceiveRequest {
    ReceiveStatus receiveStatus;

    @NotEmpty(message = "PURCHASE_DETAILS_REQUIRED")
    List<PurchaseDetailReceiveRequest> details;
}
