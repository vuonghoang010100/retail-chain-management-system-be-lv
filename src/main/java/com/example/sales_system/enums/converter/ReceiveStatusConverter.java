package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.ReceiveStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReceiveStatusConverter implements Converter<String, ReceiveStatus> {
    @Override
    public ReceiveStatus convert(@Nonnull String source) {
        for (ReceiveStatus status : ReceiveStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_RECEIVE_STATUS);
    }
}
