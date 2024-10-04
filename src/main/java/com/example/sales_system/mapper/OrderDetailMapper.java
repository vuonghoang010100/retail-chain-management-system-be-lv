package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.OrderDetailResponse;
import com.example.sales_system.entity.tenant.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDetailMapper {
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
}
