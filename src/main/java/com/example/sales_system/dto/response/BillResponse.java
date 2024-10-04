package com.example.sales_system.dto.response;

import com.example.sales_system.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Bill}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillResponse {
    Long id;
    PaymentStatus paymentStatus;
    Long total;
    String note;

    VendorResponse vendor;
    EmployeeResponseSimple employee;

    Set<PurchaseWoBillResponse> purchases;

    String createTime;
    String updateTime;
}