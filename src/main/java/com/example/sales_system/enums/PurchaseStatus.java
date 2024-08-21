package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PurchaseStatus {
    @JsonProperty("Chưa xác nhận")
    DRAFT("Chưa xác nhận"),

    @JsonProperty("Chờ nhận hàng")
    PENDING("Chờ nhận hàng"),

    @JsonProperty("Hoàn thành")
    COMPLETE("Hoàn thành"),

    @JsonProperty("Đã hủy")
    CANCEL("Đã hủy");
    private final String status;

    PurchaseStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
