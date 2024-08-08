package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.EmployeeStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class EmployeeStatusConverter implements Converter<String, EmployeeStatus> {
    @Override
    public EmployeeStatus convert(@Nonnull String source) {
        for (EmployeeStatus status : EmployeeStatus.values()) {
            if (status.getStatus().equals(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_EMPLOYEE_STATUS);
    }
}
