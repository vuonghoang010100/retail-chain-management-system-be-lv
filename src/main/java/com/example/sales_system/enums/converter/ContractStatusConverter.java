package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.ContractStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContractStatusConverter implements Converter<String, ContractStatus> {
    @Override
    public ContractStatus convert(@Nonnull String source) {
        for (ContractStatus status : ContractStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new AppException(AppStatusCode.INVALID_CONTRACT_STATUS);
    }
}
