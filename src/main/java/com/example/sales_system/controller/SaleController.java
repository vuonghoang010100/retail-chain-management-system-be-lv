package com.example.sales_system.controller;

import com.example.sales_system.dto.request.OrderCreateRequest;
import com.example.sales_system.dto.request.OrderUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.InvoiceResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.OrderResponse;
import com.example.sales_system.entity.tenant.Invoice;
import com.example.sales_system.entity.tenant.Order;
import com.example.sales_system.entity.tenant.Product;
import com.example.sales_system.service.SaleService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.util.SortBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Sales", description = "The Sales API.")
public class SaleController {
    SaleService saleService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public AppResponse<OrderResponse> createOrder(@RequestBody @Valid OrderCreateRequest request) {
        return AppResponse.<OrderResponse>builder()
                .result(saleService.createOrder(request))
                .build();
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ListResponse<OrderResponse>> getAllOrders(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Order.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Order>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Order>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)

                .build();

        return AppResponse.<ListResponse<OrderResponse>>builder()
                .result(saleService.getAllOrders(spec, pageable))
                .build();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<OrderResponse> updateOrder(@PathVariable Long id,@RequestBody @Valid OrderUpdateRequest request) {
        return AppResponse.<OrderResponse>builder()
                .result(saleService.updateOrder(id, request))
                .build();
    }



    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<OrderResponse> getOrder(@PathVariable Long id) {
        return AppResponse.<OrderResponse>builder()
                .result(saleService.getOrder(id))
                .build();
    }


    @GetMapping("/invoices")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ListResponse<InvoiceResponse>> getAllInvoices(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Invoice.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Invoice>()
                .or("id", FilterOperator.TO_STRING_LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Invoice>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)

                .build();

        return AppResponse.<ListResponse<InvoiceResponse>>builder()
                .result(saleService.getAllInvoices(spec, pageable))
                .build();
    }

}
