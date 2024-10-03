package com.example.sales_system.dto.response;

import com.example.sales_system.enums.PromoteStatus;
import com.example.sales_system.enums.PromoteType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Promote}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoteSimpleResponse {
    Long id;
    String name;
    String description;

    PromoteType type;
    Long percentage;
    Long maxDiscount;

    Long amount;

    Long discountPrice;
    ProductResponse product;

    LocalDate startDate;
    LocalDate endDate;
    Long quantity;
    PromoteStatus status;

    Long minQuantityRequired;
    Long minAmountRequired;

    String createTime;
    String updateTime;
}
