package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.CategoryCreateRequest;
import com.example.sales_system.dto.request.CategoryUpdateRequest;
import com.example.sales_system.dto.response.CategoryResponse;
import com.example.sales_system.entity.tenant.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);

    CategoryResponse toCategoryResponse(Category category);

    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest request);
}
