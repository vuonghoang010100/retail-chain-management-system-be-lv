package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum ReceiveStatus {
    @JsonProperty("Chưa nhận")
    RECEIVE("Chưa nhận"),

    @JsonProperty("Đã nhận một phần")
    PART_RECEIVED("Đã nhận một phần"),

    @JsonProperty("Đã nhận")
    RECEIVED("Đã nhận"),

    ;
    private final String status;

    ReceiveStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
