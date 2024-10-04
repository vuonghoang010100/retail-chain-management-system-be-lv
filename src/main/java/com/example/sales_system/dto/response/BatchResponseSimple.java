package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Batch}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchResponseSimple {
    Long id;
    Long quantity;
    LocalDate mfg;
    LocalDate exp;
}
