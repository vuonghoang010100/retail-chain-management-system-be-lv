package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.TransferDetail}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferDetailResponse {
    Long id;
    Long quantity;

    ProductResponse product;
    BatchResponseSimple fromBatch;
    BatchResponseSimple toBatch;

    String createTime;
    String updateTime;
}