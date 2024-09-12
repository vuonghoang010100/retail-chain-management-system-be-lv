package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PromoteType {
    @JsonProperty("Phần trăm")
    PERCENTAGE("Phần trăm"),

    @JsonProperty("Số tiền")
    AMOUNT("Số tiền"),

    ;
    private final String type;

    PromoteType(String status) {
        this.type = status;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
