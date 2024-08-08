package com.example.sales_system.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum Gender {
    @JsonProperty("Nam")
    MALE("Nam"),

    @JsonProperty("Nữ")
    FEMALE("Nữ")

    ;

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return this.gender;
    }
}
