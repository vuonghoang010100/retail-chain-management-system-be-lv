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
    ILLEGAL_ARGUMENT(-3, "Illegal argument", HttpStatus.INTERNAL_SERVER_ERROR),

    // Authenticated + Authorized error
    UNAUTHENTICATED(-100, "Unauthenticated", HttpStatus.UNAUTHORIZED), // for request doesn't have valid JWT
    UNAUTHORIZED(-101, "Unauthorized", HttpStatus.FORBIDDEN), // for request doesn't have Authorization

    EMAIL_PASSWORD_INCORRECT(-103, "Email password incorrect", HttpStatus.BAD_REQUEST),

    // Argument contraits
    INVALID_ARGUMENT(-201, "Invalid argument", HttpStatus.BAD_REQUEST),
    INVALID_DATE(-202, "Invalid date", HttpStatus.BAD_REQUEST),
    INVALID_TIME(-203, "Invalid time", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(-204, "Invalid email", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(-205, "Invalid password", HttpStatus.BAD_REQUEST),
    INVALID_GENDER(-206, "Invalid gender", HttpStatus.BAD_REQUEST),
    INVALID_PAGE(-207, "Invalid page", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_SIZE(-208, "Invalid page size", HttpStatus.BAD_REQUEST),
    INVALID_EMPLOYEE_STATUS(-209, "Invalid employee status", HttpStatus.BAD_REQUEST),
    INVALID_STORE_STATUS(-210, "Invalid store status", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_STATUS(-211, "Invalid product status", HttpStatus.BAD_REQUEST),
    INVALID_FILE_EXTENSION(-212, "Invalid file extension", HttpStatus.BAD_REQUEST),
    INVALID_VENDOR_STATUS(-213, "Invalid vendor status", HttpStatus.BAD_REQUEST),
    INVALID_CONTRACT_STATUS(-214, "Invalid contract status", HttpStatus.BAD_REQUEST),

    // Unique Contraits
    EMAIL_ALREADY_EXISTED(-300, "Email already existed", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTED(-301, "Phone already existed", HttpStatus.BAD_REQUEST),
    NAME_ALREADY_EXISTED(-302, "Name already existed", HttpStatus.BAD_REQUEST),
    FULLNAME_ALREADY_EXISTED(-303, "Fullname already existed", HttpStatus.BAD_REQUEST),
    SKU_ALREADY_EXISTED(-304, "Sku already existed", HttpStatus.BAD_REQUEST),

    // Validate contraits
    FULLNAME_IS_REQUIRED(-400, "Fullname is required", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(-401, "Email is required", HttpStatus.BAD_REQUEST),
    PHONE_IS_REQUIRED(-402, "Phone is required", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_REQUIRED(-403, "Password is required", HttpStatus.BAD_REQUEST),
    NAME_IS_REQUIRED(-404, "Name is required", HttpStatus.BAD_REQUEST),
    PROVINCE_IS_REQUIRED(-405, "Province is required", HttpStatus.BAD_REQUEST),
    DISTRICT_IS_REQUIRED(-406, "District is required", HttpStatus.BAD_REQUEST),
    ADDRESS_IS_REQUIRED(-407, "Address is required", HttpStatus.BAD_REQUEST),
    SKU_IS_REQUIRED(-408, "Sku is required", HttpStatus.BAD_REQUEST),
    BRAND_IS_REQUIRED(-409, "Brand is required", HttpStatus.BAD_REQUEST),
    PRICE_IS_REQUIRED(-410, "Price is required", HttpStatus.BAD_REQUEST),
    UNIT_IS_REQUIRED(-411, "Unit is required", HttpStatus.BAD_REQUEST),
    PRICE_MUST_BE_POSITIVE(-412, "Price must be positive", HttpStatus.BAD_REQUEST),
    FILE_MUST_BE_LESS_THAN_10MB(-413, "File length must be less than 10MB", HttpStatus.BAD_REQUEST),
    REWARD_POINT_MUST_BE_GREATER_OR_EQUAL_THAN_ZERO(-414, "Reward point must be greater or equal than zero", HttpStatus.BAD_REQUEST),

    // Multitenant API error
    TENANT_NOT_FOUND(-500, "Tenant not found", HttpStatus.NOT_FOUND),
    TENANT_INACTIVE(-501, "Tenant inactived", HttpStatus.BAD_REQUEST),

    // Master Managerment API error


    // Empoloyees API error
    EMPLOYEE_NOT_FOUND(-700, "Employee not found", HttpStatus.NOT_FOUND),


    // Roles API error
    ROLE_NOT_FOUND(-800, "Role not found", HttpStatus.NOT_FOUND),
    // Permissions API error

    // Store API error
    STORE_NOT_FOUND(-900, "Store not found", HttpStatus.NOT_FOUND),

    // Category API error
    CATEGORY_NOT_FOUND(-1000, "Category not found", HttpStatus.NOT_FOUND),

    // Product API error
    PRODUCT_NOT_FOUND(-1100, "Product not found", HttpStatus.NOT_FOUND),

    // Customer API error
    CUSTOMER_NOT_FOUND(-1200, "Customer not found", HttpStatus.NOT_FOUND),

    // Vendor API error
    VENDOR_NOT_FOUND(-1300, "Vendor not found", HttpStatus.NOT_FOUND),

    // Contract API error
    CONTRACT_NOT_FOUND(-1400, "Contract not found", HttpStatus.NOT_FOUND),

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
