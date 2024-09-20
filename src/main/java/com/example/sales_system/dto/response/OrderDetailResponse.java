package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.OrderDetail}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Long id;
    Long quantity;
    Long price;
    Long subTotal;
    Long total;
    Boolean done;
    LocalDate saleDate;

    ProductResponse product;
    StoreResponse store;
    BatchResponseSimple batch;

    String createTime;
    String updateTime;
}