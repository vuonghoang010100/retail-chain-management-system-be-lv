package com.example.sales_system.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Inventory}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCreateRequest {
    @NotNull(message = "STORE_ID_REQUIRED")
    Long storeId;

    @NotNull(message = "EMPLOYEE_ID_REQUIRED")
    Long employeeId;

    @NotEmpty(message = "INVENTORY_DETAILS_REQUIRED")
    List<InventoryDetailCreateRequest> details;
}