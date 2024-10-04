package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.BillCreateRequest;
import com.example.sales_system.dto.response.BillResponse;
import com.example.sales_system.dto.response.BillSimpleResponse;
import com.example.sales_system.entity.tenant.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BillMapper {
    BillResponse toBillResponse(Bill bill);

    BillSimpleResponse toBillSimpleResponse(Bill bill);

    Bill toBill(BillCreateRequest request);
}
