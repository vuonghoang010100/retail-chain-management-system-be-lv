package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PromoteStatus {
    @JsonProperty("Còn hiệu lực")
    ACTIVE("Còn hiệu lực"),

    @JsonProperty("Hết hiệu lực")
    INACTIVE("Hết hiệu lực"),

    ;
    private final String status;

    PromoteStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
