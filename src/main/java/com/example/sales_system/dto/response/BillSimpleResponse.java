package com.example.sales_system.dto.response;

import com.example.sales_system.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Bill}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillSimpleResponse {
    Long id;
    PaymentStatus paymentStatus;
    Long total;
    String note;

    String createTime;
    String updateTime;
}
