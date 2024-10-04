package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.PromoteType;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PromoteTypeConveter implements Converter<String, PromoteType> {
    @Override
    public PromoteType convert(@Nonnull String source) {
        for (PromoteType promoteType : PromoteType.values()) {
            if (promoteType.getType().equalsIgnoreCase(source)) {
                return promoteType;
            }
        }
        throw new AppException(AppStatusCode.INVALID_PROMOTE_TYPE);
    }
}
