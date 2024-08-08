package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum ProductStatus {
    @JsonProperty("Đang bán")
    ACTIVE("Đang bán"),

    @JsonProperty("Dừng bán")
    INACTIVE("Dừng bán"),

    ;
    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
