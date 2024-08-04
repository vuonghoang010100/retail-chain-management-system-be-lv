package com.example.sales_system.specification;

import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class FilterSpecification<T> implements Specification<T> {
    private final Filter filter;

    public FilterSpecification(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(@Nonnull Root<T> root, @Nonnull CriteriaQuery<?> query, @Nonnull CriteriaBuilder criteriaBuilder) {
        if (Objects.isNull(filter.getValue())) {
            return null;
        }

        switch (filter.getOperator()) {
            case EQUAL:
                return criteriaBuilder.equal(root.get(filter.getFieldName()), filter.getValue());
            case LIKE:
                return criteriaBuilder.like(root.get(filter.getFieldName()), "%" + filter.getValue() + "%");
            case ID_LIKE:
                return criteriaBuilder.like(root.get(filter.getFieldName()).as(String.class), "%" + filter.getValue().toString() + "%");
            case DATE_BETWEEN: {
                // NOTE: not working
                var list = (List<?>) filter.getValue();
                var expr1 = (LocalDate) list.getFirst();
                var expr2 = (LocalDate) list.getLast();
                return criteriaBuilder.between(root.get(filter.getFieldName()).as(LocalDate.class), expr1, expr2);
            }
        }
        return null;
    }
}
