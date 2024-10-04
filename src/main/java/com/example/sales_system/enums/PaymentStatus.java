package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PaymentStatus {
    @JsonProperty("Đã thanh toán")
    PAID("Đã thanh toán"),

    @JsonProperty("Chưa thanh toán")
    UNPAID("Chưa thanh toán"),

    ;
    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
