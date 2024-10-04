package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PromoteType {
    @JsonProperty("Phần trăm Hóa đơn")
    PERCENTAGE("Phần trăm"),

    @JsonProperty("Số tiền Hóa đơn")
    AMOUNT("Số tiền"),

    @JsonProperty("Giảm giá sản phẩm")
    DISCOUNTPRODUCT("Giảm giá sản phẩm");
    private final String type;

    PromoteType(String status) {
        this.type = status;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
