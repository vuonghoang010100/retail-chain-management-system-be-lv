package com.example.sales_system.dto.request;

import com.example.sales_system.enums.PurchaseStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class PurchaseUpdateRequest {
    @NotNull(message = "VENDOR_ID_REQUIRED")
    Long vendorId;

    @NotNull(message = "USE_CONTRACT_IS_REQUIRED")
    boolean useContract;

    Long contractId;

    @NotNull(message = "STORE_ID_REQUIRED")
    Long storeId;

    @NotNull(message = "EMPLOYEE_ID_REQUIRED")
    Long employeeId;

    PurchaseStatus status;

    String note;

    @NotEmpty(message = "PURCHASE_DETAILS_REQUIRED")
    List<PurchaseDetailUpdateRequest> details;
}
