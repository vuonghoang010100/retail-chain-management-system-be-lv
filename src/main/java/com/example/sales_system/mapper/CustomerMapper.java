package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.CustomerCreateRequest;
import com.example.sales_system.dto.request.CustomerUpdateRequest;
import com.example.sales_system.dto.response.CustomerResponse;
import com.example.sales_system.entity.tenant.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    Customer toCustomer(CustomerCreateRequest request);

    CustomerResponse toCustomerResponse(Customer customer);

    void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);
}
