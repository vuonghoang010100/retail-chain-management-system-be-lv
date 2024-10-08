package com.example.sales_system.entity.tenant;

import com.example.sales_system.dto.request.TransferDetailCreationRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Transfer}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferCreationRequest {
    String note;

    @NotNull(message = "STORE_ID_REQUIRED")
    Long fromStoreId;

    @NotNull(message = "STORE_ID_REQUIRED")
    Long toStoreId;

    @NotNull(message = "EMPLOYEE_ID_REQUIRED")
    Long employeeId;

    @NotEmpty(message = "DETAILS_IS_REQUIRED")
    List<TransferDetailCreationRequest> details;
}