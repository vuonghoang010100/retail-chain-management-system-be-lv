package com.example.sales_system.dto.response;

import com.example.sales_system.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Invoice}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceSimpleResponse {
    Long id;
    Long total;
    PaymentStatus paymentStatus;

    EmployeeResponseSimple employee;
    StoreResponse store;
    CustomerResponse customer;

    String createTime;
    String updateTime;
}