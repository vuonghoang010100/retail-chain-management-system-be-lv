package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.entity.tenant.Product;
import com.example.sales_system.entity.tenant.Promote;
import com.example.sales_system.entity.tenant.Store;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class PromoteSpecifications {

    public static Specification<Promote> hasEmployee(Long employeeId) {
        if (Objects.isNull(employeeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Promote, Employee> join = root.join("employees");
            return criteriaBuilder.equal(join.get("id"), employeeId);
        };
    }

    public static Specification<Promote> hasStore(Long storeId) {
        if (Objects.isNull(storeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Promote, Store> join = root.join("stores");
            return criteriaBuilder.equal(join.get("id"), storeId);
        };
    }

    public static Specification<Promote> hasProduct(Long productId) {
        if (Objects.isNull(productId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Promote, Product> join = root.join("products");
            return criteriaBuilder.equal(join.get("id"), productId);
        };
    }
}
