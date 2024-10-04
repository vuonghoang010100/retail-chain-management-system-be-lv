package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.PurchaseCreateRequest;
import com.example.sales_system.dto.response.PurchaseResponse;
import com.example.sales_system.dto.response.PurchaseWoBillResponse;
import com.example.sales_system.entity.tenant.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseMapper {
    PurchaseResponse toPurchase(Purchase purchase);

    PurchaseWoBillResponse toPurchaseWoBill(Purchase purchase);

    @Mapping(target = "details", ignore = true)
    Purchase toPurchase(PurchaseCreateRequest purchaseCreateRequest);
}
