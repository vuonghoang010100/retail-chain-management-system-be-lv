package com.example.sales_system.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Product}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {

    @NotEmpty(message = "SKU_IS_REQUIRED")
    String sku;

    @NotEmpty(message = "NAME_IS_REQUIRED")
    String name;

    @NotEmpty(message = "BRAND_IS_REQUIRED")
    String brand;

    @NotEmpty(message = "UNIT_IS_REQUIRED")
    String unit;

    //    @NotEmpty(message = "PRICE_IS_REQUIRED")
    Integer price;

    String description;

    String note;
    String imageBase64;

    Long categoryId;
}