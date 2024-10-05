package com.example.sales_system.controller;

import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Dashboard", description = "The Dashboard API.")
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/metrics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public AppResponse<Map<Object, Object>> test(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        Map<Object, Object> map = new HashMap<>();

        // Doanh thu
        map.put("revenue", dashboardService.getTotalRevenue(from, to));
        // Loi nhuan
        map.put("profit", dashboardService.getTotalProfit(from, to));
        // Tong don dat hang
        map.put("totalOrders", dashboardService.getTotalOrders(from, to));
        // aov
        map.put("avgOrderValue", dashboardService.getAvgOrderValue(from, to));

        map.put("avgBasket", dashboardService.getAvgBasket(from, to));

        map.put("totalSellProduct", dashboardService.getTotalSellProduct(from, to));

        map.put("newCustomer", dashboardService.getNewCustomer(from, to));

        map.put("topStore", dashboardService.getTopStores(from, to));

        map.put("topEmployee", dashboardService.getTopEmployees(from, to));

        map.put("topProduct", dashboardService.getTopProducts(from, to));


        return AppResponse.<Map<Object, Object>>builder()
                .result(map)
                .build();
    }
}
