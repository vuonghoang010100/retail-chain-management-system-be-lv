package com.example.sales_system.dto.response;

import com.example.sales_system.enums.PaymentStatus;
import com.example.sales_system.enums.PurchaseStatus;
import com.example.sales_system.enums.ReceiveStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Purchase}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseResponse {
    Long id;
    boolean useContract;

    LocalDate receivedDate;

    PurchaseStatus status;
    ReceiveStatus receiveStatus;
    PaymentStatus paymentStatus;

    Long total;

    VendorResponse vendor;
    ContractWoVendorResponse contract;
    StoreResponse store;
    EmployeeResponseSimple employee;

    Set<PurchaseDetailResponse> details;
}
