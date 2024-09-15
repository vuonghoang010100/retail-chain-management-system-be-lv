package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.PromoteCreateRequest;
import com.example.sales_system.dto.request.PromoteUpdateRequest;
import com.example.sales_system.dto.response.PromoteResponse;
import com.example.sales_system.dto.response.PromoteSimpleResponse;
import com.example.sales_system.entity.tenant.Promote;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromoteMapper {
    PromoteResponse toPromoteResponse(Promote promote);

    PromoteSimpleResponse toPromoteSimpleResponse(Promote promote);

    Promote toPromote(PromoteCreateRequest request);

    void updatePromote(@MappingTarget Promote promote, PromoteUpdateRequest request);
}
