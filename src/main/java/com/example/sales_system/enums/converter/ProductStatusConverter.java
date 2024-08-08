package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.ProductStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductStatusConverter implements Converter<String, ProductStatus> {
    @Override
    public ProductStatus convert(@Nonnull String source) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_EMPLOYEE_STATUS);
    }
}
