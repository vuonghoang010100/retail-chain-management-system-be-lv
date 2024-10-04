package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class PurchaseSpecifications {
    public static Specification<Purchase> hasVendor(Long vendorId) {
        if (Objects.isNull(vendorId)) return null;
        return (root, query, cb) -> {
            Join<Purchase, Vendor> vendorJoin = root.join("vendor");
            return cb.equal(vendorJoin.get("id"), vendorId);
        };
    }

    public static Specification<Purchase> hasContract(Long contractId) {
        if (Objects.isNull(contractId)) return null;
        return (root, query, cb) -> {
            Join<Purchase, Contract> contractJoin = root.join("contract");
            return cb.equal(contractJoin.get("id"), contractId);
        };
    }

    public static Specification<Purchase> hasStore(Long storeId) {
        if (Objects.isNull(storeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Purchase, Store> storeJoin = root.join("store");
            return criteriaBuilder.equal(storeJoin.get("id"), storeId);
        };
    }

    public static Specification<Purchase> hasEmployee(Long employeeId) {
        if (Objects.isNull(employeeId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Purchase, Employee> employeeJoin = root.join("employee");
            return criteriaBuilder.equal(employeeJoin.get("id"), employeeId);
        };
    }

    public static Specification<Purchase> hasProduct(Long productId) {
        if (Objects.isNull(productId)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Purchase, PurchaseDetail> detailJoin = root.join("details");
            Join<Purchase, Product> productJoin = detailJoin.join("product");
            return criteriaBuilder.equal(productJoin.get("id"), productId);
        };
    }
}
