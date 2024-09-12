package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class InventorySpecifications {
    public static Specification<Inventory> hasStore(Long storeId) {
        if (Objects.isNull(storeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Inventory, Store> storeJoin = root.join("store");
            return criteriaBuilder.equal(storeJoin.get("id"), storeId);
        };
    }

    public static Specification<Inventory> hasEmployee(Long employeeId) {
        if (Objects.isNull(employeeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Inventory, Employee> employeeJoin = root.join("employee");
            return criteriaBuilder.equal(employeeJoin.get("id"), employeeId);
        };
    }

    public static Specification<Inventory> hasProduct(Long productId) {
        if (Objects.isNull(productId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Inventory, InventoryDetail> inventoryDetailJoin = root.join("inventoryDetail");
            Join<InventoryDetail, Product> productJoin = inventoryDetailJoin.join("product");
            return criteriaBuilder.equal(productJoin.get("id"), productId);
        };
    }
}
