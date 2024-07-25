package com.example.sales_system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum AppStatusCode {
    // Success status
    OK(0, null, HttpStatus.OK),

    // Uncatch status
    UNCATEGORIZED_ERROR(-100, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    // TODO invalid key

    // Error status:
    // 1. Authenticated + Authorized error
    UNAUTHENTICATED(-200, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(-201, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(-202, "Forbidden", HttpStatus.FORBIDDEN),
    // 2. Multitenant API error

    // 3. Master Managerment API error

    // 4. Products API error

    // 5. Categories API error

    // 6. Orders API error

    // 7. Invoices API error

    // 8. x API error

    // 9. x API error

    // 10. x API error


    ;
    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    AppStatusCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    AppStatusCode(int code, String message) {
        this(code, message, HttpStatus.BAD_REQUEST);
    }
}
