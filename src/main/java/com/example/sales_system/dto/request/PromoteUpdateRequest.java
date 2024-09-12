package com.example.sales_system.dto.request;

import com.example.sales_system.enums.PromoteStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Promote}
 */
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoteUpdateRequest {
    String name;
    LocalDate endDate;
    Long quantity;
    PromoteStatus status;

    Boolean allProduct;
    List<Long> productIds;

    Boolean allStore;
    List<Long> storeIds;
}