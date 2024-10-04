package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.OrderUsePromoteResponse;
import com.example.sales_system.entity.tenant.OrderUsePromote;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderUsePromoteMapper {
    OrderUsePromoteResponse toOrderUsePromoteResponse(OrderUsePromote orderUsePromote);
}
