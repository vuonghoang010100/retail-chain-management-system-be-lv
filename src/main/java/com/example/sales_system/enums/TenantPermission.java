package com.example.sales_system.enums;

import lombok.Getter;

@Getter
public enum TenantPermission {
//    // Role
//    ROLE_READ("ROLE_READ", ""),
//    ROLE_CREATE("ROLE_CREATE", ""),
//    ROLE_UPDATE("ROLE_UPDATE", ""),
//    ROLE_DELETE("ROLE_DELETE", ""),
//    // Employee
//    EMPLOYEE_READ("EMPLOYEE_READ", ""),
//    EMPLOYEE_CREATE("EMPLOYEE_CREATE", ""),
//    EMPLOYEE_UPDATE("EMPLOYEE_UPDATE", ""),
//    EMPLOYEE_DELETE("EMPLOYEE_DELETE", ""),
//    // Product
//    PO_READ("PO_READ", ""),
//    PO_CREATE("PO_CREATE", ""),
//    PO_UPDATE("PO_UPDATE", ""),
//    PO_DELETE("PO_DELETE", ""),
//
//    // Category
//    CA_READ("CA_READ", ""),
//    CA_CREATE("CA_CREATE", ""),
//    CA_UPDATE("CA_UPDATE", ""),
//    CA_DELETE("CA_DELETE", ""),
//
//    // Sale
//    SALE_READ("SALE_READ", ""),
//
//    // customer
//    CU_READ("CU_READ", ""),
//    CU_CREATE("CU_CREATE", ""),
//    CU_UPDATE("CU_UPDATE", ""),
//    CU_DELETE("CU_DELETE", ""),
//
//    // vendor
//    VE_READ("VE_READ", ""),
//    VE_CREATE("VE_CREATE", ""),
//    VE_UPDATE("VE_UPDATE", ""),
//    VE_DELETE("VE_DELETE", ""),
//
//    // contract
//    CO_READ("CO_READ", ""),
//    CO_CREATE("CO_CREATE", ""),
//    CO_UPDATE("CO_UPDATE", ""),
//    CO_DELETE("CO_DELETE", ""),
//
//    // store
//    ST_READ("ST_READ", ""),
//    ST_CREATE("ST_CREATE", ""),
//    ST_UPDATE("ST_UPDATE", ""),
//    ST_DELETE("ST_DELETE", ""),
//
//    // inventory
//    IC_READ("IC_READ", ""),
//    IC_CREATE("IC_CREATE", ""),
//
//    // promote
//    PR_READ("PR_READ", ""),
//    PR_CREATE("PR_CREATE", ""),
//    PR_UPDATE("PR_UPDATE", ""),
//    PR_DELETE("PR_DELETE", ""),
//
//    // dashboard
//    DS_READ("DS_READ", ""),
//
//    // report
//    RP_READ("RP_READ", ""),

    DASHBOARD("DASHBOARD", "Xem Dashboard"),

    PRODUCT("PRODUCT", "Quản lý sản phẩm"),
    CATEGORY("CATEGORY", "Quản lý nhóm sản phẩm"),

    SALES("SALES", "Bán hàng"),

    PURCHASE("PURCHASE", "Quản lý nhập hàng"),

    CUSTOMER("CUSTOMER", "Quản lý khách hàng"),

    VENDOR("VENDOR", "Quản lý nhà cung cấp"),
    CONTRACT("CONTRACT", "Quản lý hợp đồng"),

    STORE("STORE", "Quản lý cửa hàng"),
    TRANSFER("TRANSFER", "Quản lý đơn vận chuyển"),
    INVENTORY_CHECK("INVENTORY_CHECK", "Quản lý kiểm kho"),

    EMPLOYEE("EMPLOYEE", "Quản lý nhân viên"),
    ROLE("ROLE", "Quản lý phân quyền"),
    PROMOTE("PROMOTE", "Quản lý khuyến mãi"),
    REPORT("REPORT", "Xem báo cáo"),

    ;
    private final String name;
    private final String description;

    TenantPermission(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
