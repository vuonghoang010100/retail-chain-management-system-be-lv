package com.example.sales_system.dto.request;

import com.example.sales_system.enums.OrderStatus;
import com.example.sales_system.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    OrderStatus status;
    PaymentStatus paymentStatus;
}
