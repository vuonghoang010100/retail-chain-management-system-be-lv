package com.example.sales_system.mapper;

import com.example.sales_system.dto.response.InvoiceResponse;
import com.example.sales_system.dto.response.InvoiceSimpleResponse;
import com.example.sales_system.entity.tenant.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {
    InvoiceResponse toInvoiceResponse(Invoice invoice);

    InvoiceSimpleResponse toInvoiceSimpleResponse(Invoice invoice);
}
