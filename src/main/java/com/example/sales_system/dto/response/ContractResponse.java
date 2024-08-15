package com.example.sales_system.dto.response;

import com.example.sales_system.enums.ContractStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO for {@link com.example.sales_system.entity.tenant.Contract}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractResponse {
    Long id;
    LocalDate startDate;
    LocalDate endDate;
    Integer period;
    LocalDate latestPurchaseDate;
    LocalDate nextPurchaseDate;
    String pdfUrl;
    ContractStatus status;
    String note;

    VendorResponse vendor;

    String createTime;
    String updateTime;
}