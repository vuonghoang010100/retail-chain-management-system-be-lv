package com.example.sales_system.specification;

import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Objects;

public class FilterSpecification<T> implements Specification<T> {
    private final Filter filter;

    public FilterSpecification(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(@Nonnull Root<T> root, @Nonnull CriteriaQuery<?> query, @Nonnull CriteriaBuilder criteriaBuilder) {
        if (Objects.isNull(filter.getValue())) return null;

        switch (filter.getOperator()) {
            case EQUAL:
                return criteriaBuilder.equal(root.get(filter.getFieldName()), filter.getValue());
            case LIKE:
                return criteriaBuilder.like(root.get(filter.getFieldName()), "%" + filter.getValue() + "%");
            case TO_STRING_LIKE:
                return criteriaBuilder.like(root.get(filter.getFieldName()).as(String.class), "%" + filter.getValue().toString() + "%");
            case GTE_DATE:
                return criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()), (LocalDate) filter.getValue());
            case LTE_DATE:
                return criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()), (LocalDate) filter.getValue());
            case BETWEEN_DATE: {
                var pair = (Pair) filter.getValue();
                var expr1 = (LocalDate) pair.getFirst();
                var expr2 = (LocalDate) pair.getSecond();
                if (Objects.isNull(expr1) || Objects.isNull(expr2)) return null;
                return criteriaBuilder.between(root.get(filter.getFieldName()).as(LocalDate.class), expr1, expr2);
            }
        }
        return null;
    }
}
