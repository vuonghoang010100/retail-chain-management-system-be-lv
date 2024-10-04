package com.example.sales_system.specification;

import com.example.sales_system.entity.tenant.Bill;
import com.example.sales_system.entity.tenant.Employee;
import com.example.sales_system.entity.tenant.Vendor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class BillSpecifications {
    public static Specification<Bill> hasVendor(Long vendorId) {
        if (Objects.isNull(vendorId)) return null;
        return (root, query, cb) -> {
            Join<Bill, Vendor> vendorJoin = root.join("vendor");
            return cb.equal(vendorJoin.get("id"), vendorId);
        };
    }

    public static Specification<Bill> hasEmployee(Long employeeId) {
        if (Objects.isNull(employeeId)) return null;
        return (root, query, cb) -> {
            Join<Bill, Employee> employeeJoin = root.join("employee");
            return cb.equal(employeeJoin.get("id"), employeeId);
        };
    }
}
