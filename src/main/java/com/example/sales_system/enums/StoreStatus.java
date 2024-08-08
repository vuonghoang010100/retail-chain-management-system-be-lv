package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum StoreStatus {
    @JsonProperty("Hoạt động")
    ACTIVE("Hoạt động"),

    @JsonProperty("Dừng hoạt động")
    INACTIVE("Dừng hoạt động"),

    ;
    private final String status;

    StoreStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
