package com.example.sales_system.dto.request;

import com.example.sales_system.enums.ProductStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class ProductUpdateRequest {

    @NotEmpty(message = "SKU_IS_REQUIRED")
    String sku;

    @NotEmpty(message = "NAME_IS_REQUIRED")
    String name;

    @NotEmpty(message = "BRAND_IS_REQUIRED")
    String brand;

    @NotEmpty(message = "UNIT_IS_REQUIRED")
    String unit;

    @NotNull(message = "PRICE_IS_REQUIRED")
    @Positive(message = "PRICE_MUST_BE_POSITIVE")
    Long price;

    String description;
    ProductStatus status;
    String note;
    String imageBase64;

    Long categoryId;
}
