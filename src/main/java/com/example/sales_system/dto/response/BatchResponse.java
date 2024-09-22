package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchResponse {
    Long id;
    Long quantity;
    LocalDate mfg;
    LocalDate exp;
    StoreResponse store;
    ProductResponse product;
}
