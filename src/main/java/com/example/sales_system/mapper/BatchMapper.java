package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.BatchResponseSimple;
import com.example.sales_system.entity.tenant.Batch;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BatchMapper {
    BatchResponseSimple toBatchResponseSimple(Batch batch);
}
