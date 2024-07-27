package com.example.sales_system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum AppStatusCode {
    // Success status
    OK(0, null, HttpStatus.OK),

    // Error
    // General server error
    UNCATEGORIZED_ERROR(-1, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(-2, "Invalid argument", HttpStatus.INTERNAL_SERVER_ERROR),

    // Authenticated + Authorized error
    UNAUTHENTICATED(-100, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(-101, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(-102, "Forbidden", HttpStatus.FORBIDDEN),
    EMAIL_NOT_FOUND(-103, "Email not found", HttpStatus.NOT_FOUND),

    // Argument contraits
    INVALID_ARGUMENT(-201, "Invalid argument", HttpStatus.BAD_REQUEST),
    INVALID_DATE(-202, "Invalid date", HttpStatus.BAD_REQUEST),
    INVALID_TIME(-203, "Invalid time", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(-204, "Invalid email", HttpStatus.BAD_REQUEST),

    // Unique Contraits
    EMAIL_ALREADY_EXISTED(-300, "Email already existed", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTED(-301, "Phone already existed", HttpStatus.BAD_REQUEST),

    // Validate contraits
    FULLNAME_IS_REQUIRED(-400, "Fullname is required", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(-401, "Email is required", HttpStatus.BAD_REQUEST),
    PHONE_IS_REQUIRED(-402, "Phone is required", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_REQUIRED(-403, "Password is required", HttpStatus.BAD_REQUEST),

    // Multitenant API error
    TENANT_NOT_FOUND(-500, "Tenant not found", HttpStatus.NOT_FOUND),
    TENANT_INACTIVE(-501, "Tenant inactived", HttpStatus.BAD_REQUEST),

    // Master Managerment API error


    // Empoloyees API error
    EMPLOYEE_NOT_FOUND(-700, "Employee not found", HttpStatus.NOT_FOUND),


    // Roles API error
    ROLE_NOT_FOUND(-800, "Role not found", HttpStatus.NOT_FOUND),

    // Permissions API error

    // Invoices API error

    // x API error


    ;
    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    AppStatusCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
