package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.VendorCreateRequest;
import com.example.sales_system.dto.request.VendorUpdateRequest;
import com.example.sales_system.dto.response.VendorResponse;
import com.example.sales_system.dto.response.VendorWithContractResponse;
import com.example.sales_system.entity.tenant.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VendorMapper {
    Vendor toVendor(VendorCreateRequest request);

    VendorResponse toVendorResponse(Vendor vendor);

    VendorWithContractResponse toVendorWithContractResponse(Vendor vendor);

    void updateVendor(@MappingTarget Vendor vendor, VendorUpdateRequest request);
}
