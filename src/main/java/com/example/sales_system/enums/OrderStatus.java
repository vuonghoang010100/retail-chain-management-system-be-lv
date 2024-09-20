package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum OrderStatus {
    @JsonProperty("Chờ xác nhận")
    PENDING("Chờ xác nhận"),

    @JsonProperty("Đang giao hàng")
    DELIVERING("Đang giao hàng"),

    @JsonProperty("Hoàn thành")
    COMPLETE("Hoàn thành"),

    @JsonProperty("Hủy")
    CANCEL("Hủy");
    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
