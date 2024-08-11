package com.example.sales_system.controller;

import com.example.sales_system.dto.request.ProductCreateRequest;
import com.example.sales_system.dto.request.ProductUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.ProductResponse;
import com.example.sales_system.entity.tenant.Product;
import com.example.sales_system.enums.ProductStatus;
import com.example.sales_system.service.ProductService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.specification.ProductSpecifications;
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
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product", description = "The Product API.")
public class ProductController {
    ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ListResponse<ProductResponse>> getAllProducts(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer gtePrice,
            @RequestParam(required = false) Integer ltePrice,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) Long categoryId
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Product.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Product>()
                .or("sku", FilterOperator.LIKE, search)
                .or("name", FilterOperator.LIKE, search)
                .or("brand", FilterOperator.LIKE, search)
                .build();

        // Filter
        var spec = new FilterSpecificationBuilder<Product>()
                .and(searchSpec)
                .and("id", FilterOperator.TO_STRING_LIKE, id)
                .and("sku", FilterOperator.LIKE, sku)
                .and("name", FilterOperator.LIKE, name)
                .and("brand", FilterOperator.LIKE, brand)
                .and("price", FilterOperator.EQUAL, price)
                .and("price", FilterOperator.GTE_INT, gtePrice)
                .and("price", FilterOperator.LTE_INT, ltePrice)
                .and("description", FilterOperator.LIKE, description)
                .and("status", FilterOperator.EQUAL, status)
                .and("note", FilterOperator.LIKE, note)
                .and(ProductSpecifications.hasCategory(categoryId))
                .build();

        return AppResponse.<ListResponse<ProductResponse>>builder()
                .result(productService.getAllProducts(spec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ProductResponse> getProduct(@PathVariable Long id) {
        return AppResponse.<ProductResponse>builder()
                .result(productService.getProduct(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public AppResponse<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        return AppResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public AppResponse<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductUpdateRequest request) {
        return AppResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
