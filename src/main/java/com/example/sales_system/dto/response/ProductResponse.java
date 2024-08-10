package com.example.sales_system.dto.response;

import com.example.sales_system.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Product}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String sku;
    String name;
    String brand;
    String unit;
    Integer price;
    String description;
    ProductStatus status;
    String note;
    String imageUrl;
    CategoryResponse category;

    String createTime;
    String updateTime;
}