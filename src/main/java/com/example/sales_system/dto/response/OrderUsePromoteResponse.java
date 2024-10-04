package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.OrderUsePromote}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUsePromoteResponse {
    Long id;
    PromoteSimpleResponse promote;
    Long discount;
}
