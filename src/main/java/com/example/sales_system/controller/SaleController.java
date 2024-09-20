package com.example.sales_system.controller;

import com.example.sales_system.dto.request.OrderCreateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.OrderResponse;
import com.example.sales_system.service.SaleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
}
