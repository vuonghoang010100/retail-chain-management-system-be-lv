package com.example.sales_system.mapper;

import com.example.sales_system.entity.tenant.Transfer;
import com.example.sales_system.entity.tenant.TransferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransferMapper {
    TransferResponse toResponse(Transfer transfer);
}
