package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.InventoryDetailResponse;
import com.example.sales_system.entity.tenant.InventoryDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryDetailMapper {
    InventoryDetailResponse toResponse(InventoryDetail inventoryDetail);


}
