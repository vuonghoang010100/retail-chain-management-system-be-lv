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
    INVALID_PURCHASE_STATUS(-215, "Invalid purchase status", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_STATUS(-216, "Invalid payment status", HttpStatus.BAD_REQUEST),
    INVALID_RECEIVE_STATUS(-217, "Invalid receive status", HttpStatus.BAD_REQUEST),
    INVALID_PURCHASE_DETAILS(-218, "Invalid purchase details", HttpStatus.BAD_REQUEST),
    CANT_NOT_UPDATE_PURCHASE(-219, "Can't update purchase", HttpStatus.BAD_REQUEST),
    ONLY_RECEIVE_PURCHASE_WITH_STATUS_PENDING(-220, "Only receive purchase with status pending", HttpStatus.BAD_REQUEST),
    PURCHASE_RECEIVE_AMOUNT_IS_REQUIRED(-221, "Purchase receiveAmount is required", HttpStatus.BAD_REQUEST),
    PURCHASE_RECEIVE_AMOUNT_MUST_BE_POSITIVE(-222, "Purchase receiveAmount must be positive", HttpStatus.BAD_REQUEST),
    INVALID_PROMOTE_TYPE(-223, "Invalid promote type", HttpStatus.BAD_REQUEST),
    INVALID_PROMOTE_STATUS(-224, "Invalid promote status", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS(-225, "Invalid order status", HttpStatus.BAD_REQUEST),


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
    START_DATE_IS_REQUIRED(-415, "Start date is required", HttpStatus.BAD_REQUEST),
    END_DATE_IS_REQUIRED(-416, "End date is required", HttpStatus.BAD_REQUEST),
    PERIOD_IS_REQUIRED(-417, "Period is required", HttpStatus.BAD_REQUEST),
    PERIOD_MUST_BE_POSITIVE(-418, "Period must be positive", HttpStatus.BAD_REQUEST),
    VENDOR_ID_REQUIRED(-419, "Vendor id is required", HttpStatus.BAD_REQUEST),
    USE_CONTRACT_IS_REQUIRED(-420, "Use contract is required", HttpStatus.BAD_REQUEST),
    CONTRACT_ID_IS_REQUIRED(-421, "Contract is required", HttpStatus.BAD_REQUEST),
    STORE_ID_REQUIRED(-422, "Store id is required", HttpStatus.BAD_REQUEST),
    EMPLOYEE_ID_REQUIRED(-423, "Employee id is required", HttpStatus.BAD_REQUEST),
    PURCHASE_DETAILS_REQUIRED(-424, "Purchase details is required", HttpStatus.BAD_REQUEST),
    PRODUCT_ID_IS_REQUIRED(-425, "Product id is required", HttpStatus.BAD_REQUEST),
    PURCHASE_PRICE_IS_REQUIRED(-426, "Purchase price is required", HttpStatus.BAD_REQUEST),
    PURCHASE_PRICE_MUST_BE_POSITIVE(-427, "Purchase price must be positive", HttpStatus.BAD_REQUEST),
    PURCHASE_AMOUNT_IS_REQUIRED(-428, "Purchase amount is required", HttpStatus.BAD_REQUEST),
    PURCHASE_AMOUNT_MUST_BE_POSITIVE(-429, "Purchase amount must be positive", HttpStatus.BAD_REQUEST),
    PURCHASE_RECEIVE_MUST_NOT_BE_NOT_RECEIVED(-430, "Purchase receive must be not be not received", HttpStatus.BAD_REQUEST),
    PURCHASE_LIST_ID_IS_REQUIRED(-431, "Purchase list id is required", HttpStatus.BAD_REQUEST),
    PAYMENT_STATUS_IS_REQUIRED(-432, "Payment status is required", HttpStatus.BAD_REQUEST),
    BATCH_ID_IS_REQUIRED(-433, "Batch id is required", HttpStatus.BAD_REQUEST),
    REAL_QUANTITY_IS_REQUIRED(-434, "Real quantity is required", HttpStatus.BAD_REQUEST),
    INVENTORY_DETAILS_REQUIRED(-434, "Inventory's details is required", HttpStatus.BAD_REQUEST),
    QUANTITY_IS_REQUIRED(-435, "Quantity is required", HttpStatus.BAD_REQUEST),
    DETAILS_IS_REQUIRED(-436, "Details is required", HttpStatus.BAD_REQUEST),


    // Multitenant API error
    TENANT_NOT_FOUND(-500, "Tenant not found", HttpStatus.NOT_FOUND),
    TENANT_INACTIVE(-501, "Tenant inactived", HttpStatus.BAD_REQUEST),

    // Master Management API error


    // Employees API error
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

    // Purchase API error
    PURCHASE_NOT_FOUND(-1500, "Purchase not found", HttpStatus.NOT_FOUND),
    PURCHASE_ON_VENDOR_MUST_BE_ACTIVE(-1501, "Purchase's vendor must be active", HttpStatus.BAD_REQUEST),
    PURCHASE_ON_CONTRACT_MUST_BE_ACTIVE(-1502, "Purchase's contract must be active", HttpStatus.BAD_REQUEST),
    PURCHASE_ON_STORE_MUST_BE_ACTIVE(-1503, "Purchase's store must be active", HttpStatus.BAD_REQUEST),
    PURCHASE_ON_EMPLOYEE_MUST_BE_ACTIVE(-1504, "Purchase's employee must be active", HttpStatus.BAD_REQUEST),
    PURCHASE_ON_PRODUCT_MUST_BE_ACTIVE(-1505, "Purchase's product must be active", HttpStatus.BAD_REQUEST),
    INVALID_UPDATE_PURCHASE_STATUS(-1506, "Invalid update purchase status: PENDING, COMPLETE", HttpStatus.BAD_REQUEST),

    // Bill API error
    BILL_EMPLOYEE_MUST_BE_ACTIVE(-1600, "Bill's employee must be active", HttpStatus.BAD_REQUEST),
    BILL_PURCHASE_MUST_BE_UNPAID(-1601, "Bill's purchase must be unpaid", HttpStatus.BAD_REQUEST),
    BILL_NOT_FOUND(-1602, "Bill not found", HttpStatus.BAD_REQUEST),
    BILL_PAYMENT_STATUS_CAN_NOT_REVERT(-1603, "Bill's payment status can not revert", HttpStatus.BAD_REQUEST),
    UNABLE_TO_DELETE_PAID_BILL(-1604, "Unable to delete paid bill", HttpStatus.BAD_REQUEST),

    // Inventory API error
    INVENTORY_NOT_FOUND(-1700, "Inventory not found", HttpStatus.BAD_REQUEST),

    // Batch API error
    BATCH_NOT_FOUND(-1800, "Batch not found", HttpStatus.BAD_REQUEST),
    INVENTORY_DETAIL_MUST_NOT_BE_EMPTY(-1801, "Inventory's details must not be empty", HttpStatus.BAD_REQUEST),
    BATCH_MUST_BE_IN_THE_SAME_STORE(-1802, "Batch's store must be in same store", HttpStatus.BAD_REQUEST),

    // Promote API error
    PROMOTE_NOT_FOUND(-1900, "Promote not found", HttpStatus.BAD_REQUEST),
    PRODUCT_LIST_MUST_NOT_BE_EMPTY(-1901, "Product list must not be empty", HttpStatus.BAD_REQUEST),
    STORE_LIST_MUST_NOT_BE_EMPTY(-1902, "Store list must not be empty", HttpStatus.BAD_REQUEST),

    // Orders API error
    ORDER_ON_EMPLOYEE_MUST_BE_ACTIVE(-2000, "order's employee must be active", HttpStatus.BAD_REQUEST),
    ORDER_ON_STORE_MUST_BE_ACTIVE(-2001, "Order's store must be active", HttpStatus.BAD_REQUEST),
    PROMOTE_IS_INACTIVE(-2002, "Promote is inactive", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_CREATION_STATUS(-2003, "Invalid order creation status", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_QUANTITY(-2004, "Invalid product quantity", HttpStatus.BAD_REQUEST),
    PROMOTE_MIN_QUANTITY_REQUIRED_DOES_NOT_MATCHED(-2005, "Promote's minimum quantity required does not matched", HttpStatus.BAD_REQUEST),
    PROMOTE_MIN_AMOUNT_REQUIRED_DOES_NOT_MATCHED(-2006, "Promote's minimum amount required does not matched", HttpStatus.BAD_REQUEST),

    // Invoices API error
    ORDER_NOT_FOUND(-2007, "Order not found", HttpStatus.BAD_REQUEST),
    INVOICE_NOT_FOUND(-2008, "Invoice not found", HttpStatus.BAD_REQUEST),

    // x API error
    TRANSFER_NOT_FOUND(-2100, "Transfer not found", HttpStatus.BAD_REQUEST),
    INVALID_TRANSFER_STATUS(-2101, "Invalid transfer status", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY(-2102, "Invalid quantity", HttpStatus.BAD_REQUEST),
    TRANSFER_DETAIL_MUST_NOT_BE_EMPTY(-2103, "Transfer's details must not be empty", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_COMPLETE_TRANSFER(-2104, "Can't update complete transfer", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_CANCEL_TRANSFER(-2105, "Can't update cancel transfer", HttpStatus.BAD_REQUEST),

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
