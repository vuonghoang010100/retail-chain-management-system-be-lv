package com.example.sales_system.enums.converter;

import com.example.sales_system.enums.Gender;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenderConverter implements Converter<String, Gender> {
    @Override
    public Gender convert(@Nonnull String value) {
        for (Gender gender : Gender.values()) {
            if (gender.getGender().equals(value)) {
                return gender;
            }
        }
        throw new AppException(AppStatusCode.INVALID_GENDER);
    }
}
