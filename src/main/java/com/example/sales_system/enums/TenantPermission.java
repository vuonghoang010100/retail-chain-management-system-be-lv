package com.example.sales_system.enums;

import lombok.Getter;

@Getter
public enum TenantPermission {
    // Employee
    EMPLOYEE_READ("EMPLOYEE_READ", ""),
    EMPLOYEE_CREATE("EMPLOYEE_CREATE", ""),
    EMPLOYEE_UPDATE("EMPLOYEE_UPDATE", ""),
    EMPLOYEE_DELETE("EMPLOYEE_DELETE", ""),



    ;
    private final String name;
    private final String description;

    private TenantPermission(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
