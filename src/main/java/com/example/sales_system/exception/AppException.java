package com.example.sales_system.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    private AppStatusCode appStatusCode;

    public AppException(AppStatusCode appStatusCode) {
        super(appStatusCode.getMessage());
        this.appStatusCode = appStatusCode;
    }
}
