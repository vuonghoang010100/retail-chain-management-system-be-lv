package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.entity.tenant.Product;
import com.example.sales_system.entity.tenant.Store;
import com.example.sales_system.entity.tenant.Transfer;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class TransferSpecifications {
    public static Specification<Transfer> hasEmployee(Long employeeId) {
        if (Objects.isNull(employeeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Transfer, Employee> employeeJoin = root.join("employee");
            return criteriaBuilder.equal(employeeJoin.get("id"), employeeId);
        };
    }

    public static Specification<Transfer> hasProduct(Long productId) {
        if (Objects.isNull(productId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Transfer, Product> productJoin = root.join("product");
            return criteriaBuilder.equal(productJoin.get("id"), productId);
        };
    }

    public static Specification<Transfer> hasFromStore(Long storeId) {
        if (Objects.isNull(storeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Transfer, Store> storeJoin = root.join("fromStore");
            return criteriaBuilder.equal(storeJoin.get("id"), storeId);
        };
    }

    public static Specification<Transfer> hasToStore(Long storeId) {
        if (Objects.isNull(storeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Transfer, Store> storeJoin = root.join("toStore");
            return criteriaBuilder.equal(storeJoin.get("id"), storeId);
        };
    }
}
