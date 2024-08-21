package com.example.sales_system.specification;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterSpecificationBuilder<T> {

    private final List<Specification_<T>> specifications = new ArrayList<>();

    public FilterSpecificationBuilder<T> with(String fieldName, FilterOperator operator, Object value, Boolean isOr) {
        var filter = new Filter(fieldName, operator, value);
        return with(filter, isOr);
    }

    public FilterSpecificationBuilder<T> and(String fieldName, FilterOperator operator, Object value) {
        return with(fieldName, operator, value, Boolean.FALSE);
    }

    public FilterSpecificationBuilder<T> or(String fieldName, FilterOperator operator, Object value) {
        return with(fieldName, operator, value, Boolean.TRUE);
    }

    public FilterSpecificationBuilder<T> with(Filter filter, Boolean isOr) {
        specifications.add(new Specification_<>(new FilterSpecification<>(filter), isOr));
        return this;
    }

    public FilterSpecificationBuilder<T> and(Filter filter) {
        return with(filter, Boolean.FALSE);
    }

    public FilterSpecificationBuilder<T> or(Filter filter) {
        return with(filter, Boolean.TRUE);
    }

    public FilterSpecificationBuilder<T> with(Specification<T> specification, Boolean isOr) {
        specifications.add(new Specification_<>(specification, isOr));
        return this;
    }

    public FilterSpecificationBuilder<T> and(Specification<T> specification) {
        return with(specification, Boolean.FALSE);
    }

    public FilterSpecificationBuilder<T> or(Specification<T> specification) {
        return with(specification, Boolean.TRUE);
    }

    public Specification<T> build() {
        var list = specifications.stream()
                .filter(spec_ -> Objects.nonNull(spec_.getSpecification()))
                .toList();
        if (list.isEmpty()) {
            return null;
        }

        if (list.size() == 1) {
            return list.getFirst().getSpecification();
        }

        Specification<T> specification = list.getFirst().getSpecification();

        for (int i = 1; i < list.size(); i++) {
            var spec_ = list.get(i);
            specification = spec_.isOr()
                    ? Specification.where(specification).or(spec_.getSpecification())
                    : Specification.where(specification).and(spec_.getSpecification());
        }

        return specification;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Specification_<T> {
        private Specification<T> specification;
        private boolean isOr;
    }
}
