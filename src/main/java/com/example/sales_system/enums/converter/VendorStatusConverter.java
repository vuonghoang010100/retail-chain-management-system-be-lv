package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.VendorStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VendorStatusConverter implements Converter<String, VendorStatus> {
    @Override
    public VendorStatus convert(@Nonnull String source) {
        for (VendorStatus status : VendorStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_VENDOR_STATUS);
    }
}
