package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.OrderStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

public class OrderStatusConveter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(@Nonnull String source) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equalsIgnoreCase(source)) {
                return orderStatus;
            }
        }
        throw new AppException(AppStatusCode.INVALID_ORDER_STATUS);
    }
}
