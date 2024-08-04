package com.example.sales_system.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Objects;

public class SortBuilder {

    public static Sort buildSort(String sortQuery, Class<?> clazz) {
        // sortQuery example: -createTime,+fullName
        if (StringUtils.isEmpty(sortQuery)) {
            return Sort.unsorted();
        }

        return Sort.by(
                Arrays.stream(sortQuery.split(","))
                        .map(sort -> buildSortOrder(sort, clazz))
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    private static Sort.Order buildSortOrder(String sort, Class<?> clazz) {
        String field = sort.substring(1);

        try {
            clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            if (!field.equals("createTime") && !field.equals("updateTime")) {
                return null;
            }
        }

        if (sort.startsWith("-")) {
            return new Sort.Order(Sort.Direction.DESC, field);
        } else if (sort.startsWith("+")) {
            return new Sort.Order(Sort.Direction.ASC, field);
        }
        return null;
    }
}
