package com.example.sales_system.dto.response;

import com.example.sales_system.enums.PromoteStatus;
import com.example.sales_system.enums.PromoteType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Promote}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoteResponse {
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

    EmployeeResponseSimple employee;

    Boolean allStore;
    Set<StoreResponse> stores;

    String createTime;
    String updateTime;
}