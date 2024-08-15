package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.Contract;
import com.example.sales_system.entity.tenant.Vendor;
import jakarta.persistence.criteria.Join;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ContractSpecifications {
    public static Specification<Contract> hasVendorfullNameLike(String fullName) {
        if (StringUtils.isBlank(fullName)) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Contract, Vendor> vendor = root.join("vendor");
            return criteriaBuilder.like(vendor.get("fullName"), "%" + fullName + "%");
        };
    }

    public static Specification<Contract> hasVendorId(Long id) {
        if (id == null) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Contract, Vendor> vendor = root.join("vendor");
            return criteriaBuilder.equal(vendor.get("id"), id);
        };
    }
}
