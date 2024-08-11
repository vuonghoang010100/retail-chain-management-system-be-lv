package com.example.sales_system.specification;

import lombok.Getter;

@Getter
public enum FilterOperator {
    LIKE,
    EQUAL,
    TO_STRING_LIKE,

    BETWEEN_DATE,
    GTE_DATE,
    LTE_DATE,

    GTE_INT,
    LTE_INT,
}
