package com.example.sales_system.service;

import com.example.sales_system.dto.request.CategoryCreateRequest;
import com.example.sales_system.dto.request.CategoryUpdateRequest;
import com.example.sales_system.dto.response.CategoryResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Category;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.CategoryMapper;
import com.example.sales_system.repository.tenant.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<CategoryResponse> getAllCategories(Specification<Category> spec, Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(spec, pageable);

        return ListResponse.<CategoryResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public CategoryResponse getResponseResponseById(Long id) {
        return categoryMapper.toCategoryResponse(getCategoryById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = categoryMapper.toCategory(request);
        return categoryMapper.toCategoryResponse(saveCategory(category));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        var category = getCategoryById(id);
        categoryMapper.updateCategory(category, request);
        return categoryMapper.toCategoryResponse(saveCategory(category));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // ----- Helper functions -----

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.CATEGORY_NOT_FOUND));
    }

    private Category saveCategory(Category category) {
        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException exception) {
            if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (name)")) {
                throw new AppException(AppStatusCode.NAME_ALREADY_EXISTED);
            }
            log.debug(exception.getMessage());
            throw exception;
        }
        return category;
    }

}
