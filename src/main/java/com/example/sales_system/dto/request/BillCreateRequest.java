package com.example.sales_system.dto.request;

import com.example.sales_system.enums.PaymentStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Bill}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillCreateRequest {
    PaymentStatus paymentStatus;
    String note;

    @NotNull(message = "VENDOR_ID_REQUIRED")
    Long vendorId;

    @NotNull(message = "EMPLOYEE_ID_REQUIRED")
    Long employeeId;

    @NotEmpty(message = "PURCHASE_LIST_ID_IS_REQUIRED")
    List<Long> purchaseIds;
}