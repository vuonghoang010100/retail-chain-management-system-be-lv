package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.Category;
import com.example.sales_system.entity.tenant.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ProductSpecifications {
    public static Specification<Product> hasCategory(Long categoryId) {
        if (Objects.isNull(categoryId)) return null;
        return ((root, query, criteriaBuilder) -> {
            Join<Product, Category> join = root.join("category");
            return criteriaBuilder.equal(join.get("id"), categoryId);
        });
    }

}
