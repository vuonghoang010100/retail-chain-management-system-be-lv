package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.StoreCreateRequest;
import com.example.sales_system.dto.request.StoreUpdateRequest;
import com.example.sales_system.dto.response.StoreResponse;
import com.example.sales_system.entity.tenant.Store;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreMapper {

    Store toStore(StoreCreateRequest request);

    StoreResponse toStoreResponse(Store store);

    void updateStore(@MappingTarget Store store, StoreUpdateRequest request);

}
