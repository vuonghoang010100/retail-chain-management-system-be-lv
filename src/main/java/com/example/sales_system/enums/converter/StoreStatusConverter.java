package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.StoreStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StoreStatusConverter implements Converter<String, StoreStatus> {
    @Override
    public StoreStatus convert(@Nonnull String source) {
        for (StoreStatus status : StoreStatus.values()) {
            if (status.getStatus().equals(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_STORE_STATUS);
    }
}
