package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.PurchaseDetailCreateRequest;
import com.example.sales_system.dto.response.PurchaseDetailResponse;
import com.example.sales_system.entity.tenant.PurchaseDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseDetailMapper {
    PurchaseDetailResponse toResponse(PurchaseDetail purchaseDetail);


    PurchaseDetail toPurchaseDetail(PurchaseDetailCreateRequest purchaseDetailCreateRequest);
}
