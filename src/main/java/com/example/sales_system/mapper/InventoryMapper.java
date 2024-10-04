package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.InventoryResponse;
import com.example.sales_system.entity.tenant.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {
    InventoryResponse toResponse(Inventory inventory);
}
