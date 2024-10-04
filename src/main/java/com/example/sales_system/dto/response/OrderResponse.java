package com.example.sales_system.dto.response;

import com.example.sales_system.entity.tenant.OrderUsePromote;
import com.example.sales_system.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Order}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    Long tax;
    Long taxPercentage;
    Long subTotal;
    Long discount;
    Long total;
    OrderStatus status;

    EmployeeResponseSimple employee;
    StoreResponse store;
    CustomerResponse customer;

    InvoiceSimpleResponse invoice;

    Set<OrderUsePromoteResponse> usePromotes;

    Set<OrderDetailResponse> details;

    String createTime;
    String updateTime;
}