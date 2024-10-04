package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.PurchaseStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PurchaseStatusConverter implements Converter<String, PurchaseStatus> {
    @Override
    public PurchaseStatus convert(@Nonnull String source) {
        for (PurchaseStatus status : PurchaseStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_PURCHASE_STATUS);
    }
}
