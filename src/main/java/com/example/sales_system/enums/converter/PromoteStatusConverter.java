package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.PromoteStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PromoteStatusConverter implements Converter<String, PromoteStatus> {
    @Override
    public PromoteStatus convert(@Nonnull String source) {
        for (PromoteStatus promoteStatus : PromoteStatus.values()) {
            if (promoteStatus.getStatus().equals(source)) {
                return promoteStatus;
            }
        }
        throw new AppException(AppStatusCode.INVALID_PROMOTE_STATUS);
    }
}
