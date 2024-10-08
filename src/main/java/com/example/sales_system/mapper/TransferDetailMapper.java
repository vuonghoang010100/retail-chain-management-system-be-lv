package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.TransferDetailResponse;
import com.example.sales_system.entity.tenant.TransferDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransferDetailMapper {
    TransferDetailResponse toResponse(TransferDetail transferDetail);
}
