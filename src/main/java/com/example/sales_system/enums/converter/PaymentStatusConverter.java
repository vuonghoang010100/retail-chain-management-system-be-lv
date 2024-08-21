package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.PaymentStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusConverter implements Converter<String, PaymentStatus> {
    @Override
    public PaymentStatus convert(@Nonnull String source) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_PAYMENT_STATUS);
    }
}
