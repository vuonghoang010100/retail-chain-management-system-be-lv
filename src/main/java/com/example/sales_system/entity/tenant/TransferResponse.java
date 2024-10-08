package com.example.sales_system.entity.tenant;

import com.example.sales_system.dto.response.EmployeeResponseSimple;
import com.example.sales_system.dto.response.StoreResponse;
import com.example.sales_system.dto.response.TransferDetailResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Transfer}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferResponse {
    Long id;
    String status;
    String note;

    StoreResponse fromStore;
    StoreResponse toStore;

    EmployeeResponseSimple employee;

    Set<TransferDetailResponse> details;

    String createTime;
    String updateTime;
}