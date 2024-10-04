package com.example.sales_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.InventoryDetail}
 */
@AllArgsConstructor
@Getter
public class InventoryDetailResponse implements Serializable {
    Long id;
    Long realQuantity;
    Long dbQuantity;
    Long differenceQuantity;

    ProductResponse product;
    BatchResponseSimple batch;

    String createTime;
    String updateTime;
}