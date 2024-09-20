package com.example.sales_system.dto.request;

import com.example.sales_system.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Order}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest {
    Long taxPercentage;

    OrderStatus status;

    Long employeeId;
    Long storeId;
    Long customerId;

    List<Long> promoteIds;

    List<OrderDetailCreateRequest> details;
}