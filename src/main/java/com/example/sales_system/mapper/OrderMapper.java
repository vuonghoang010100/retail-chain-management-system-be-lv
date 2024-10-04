package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.OrderCreateRequest;
import com.example.sales_system.dto.response.OrderResponse;
import com.example.sales_system.dto.response.OrderSimpleResponse;
import com.example.sales_system.entity.tenant.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    OrderSimpleResponse toOrderSimpleResponse(Order order);

    @Mapping(target = "details", ignore = true)
    Order toOrder(OrderCreateRequest request);
}
