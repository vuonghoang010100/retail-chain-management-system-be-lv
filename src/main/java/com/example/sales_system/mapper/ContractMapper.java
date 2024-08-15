package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.ContractCreateRequest;
import com.example.sales_system.dto.request.ContractUpdateRequest;
import com.example.sales_system.dto.response.ContractResponse;
import com.example.sales_system.entity.tenant.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContractMapper {
    @Mapping(target = "vendor", ignore = true)
    Contract toContract(ContractCreateRequest request);

    ContractResponse toContractResponse(Contract contract);

    @Mapping(target = "vendor", ignore = true)
    void updateContract(@MappingTarget Contract contract, ContractUpdateRequest request);
}
