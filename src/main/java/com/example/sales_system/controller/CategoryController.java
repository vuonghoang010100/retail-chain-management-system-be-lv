package com.example.sales_system.controller;

import com.example.sales_system.dto.request.CategoryCreateRequest;
import com.example.sales_system.dto.request.CategoryUpdateRequest;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.dto.response.CategoryResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Category;
import com.example.sales_system.service.CategoryService;
import com.example.sales_system.specification.FilterOperator;
import com.example.sales_system.specification.FilterSpecificationBuilder;
import com.example.sales_system.util.SortBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Category", description = "The Category API.")
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY')")
    public AppResponse<ListResponse<CategoryResponse>> getAllCategories(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        // Convert for fe
        page = page - 1;

        // Page
        Pageable pageable = PageRequest.of(page, size, SortBuilder.buildSort(sort, Category.class));

        // Search
        var searchSpec = new FilterSpecificationBuilder<Category>()
                .or("name", FilterOperator.LIKE, search)
                .build();

        return AppResponse.<ListResponse<CategoryResponse>>builder()
                .result(categoryService.getAllCategories(searchSpec, pageable))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY')")
    public AppResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return AppResponse.<CategoryResponse>builder()
                .result(categoryService.getResponseResponseById(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY')")
    public AppResponse<CategoryResponse> createCategory(@RequestBody CategoryCreateRequest request) {
        return AppResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY')")
    public AppResponse<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request) {
        return AppResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

}
