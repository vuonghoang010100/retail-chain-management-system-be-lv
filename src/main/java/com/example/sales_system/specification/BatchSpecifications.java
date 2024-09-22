package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.Batch;
import com.example.sales_system.entity.tenant.Product;
import com.example.sales_system.entity.tenant.Store;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class BatchSpecifications {
    public static Specification<Batch> hasProductId (Long productId) {
        if (Objects.isNull(productId)) return null;
        return (root, query, cb) -> {
            Join<Batch, Product> join = root.join("product");
            return cb.equal(join.get("id"), productId);
        };
    }

    public static Specification<Batch> hasStoreId(Long storeId) {
        if (Objects.isNull(storeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Batch, Store> join = root.join("store");
            return criteriaBuilder.equal(join.get("id"), storeId);
        };
    }

    public static Specification<Batch> hasProductName (String productName) {
        if (Objects.isNull(productName)) return null;
        return (root, query, cb) -> {
            Join<Batch, Product> join = root.join("product");
            return cb.equal(join.get("name"), productName);
        };
    }

    public static Specification<Batch> hasProductCode (String sku) {
        if (Objects.isNull(sku)) return null;
        return (root, query, cb) -> {
            Join<Batch, Product> join = root.join("product");
            return cb.equal(join.get("sku"), sku);
        };
    }
}
