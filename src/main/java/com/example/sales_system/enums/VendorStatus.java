package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum VendorStatus {
    @JsonProperty("Đang hợp tác")
    ACTIVE("Đang hợp tác"),

    @JsonProperty("Dừng hợp tác")
    INACTIVE("Dừng hợp tác"),

    ;
    private final String status;

    VendorStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
