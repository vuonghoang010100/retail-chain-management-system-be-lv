package com.example.sales_system.specification;

import lombok.*;
import lombok.experimental.FieldDefaults;

//import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Filter {
    String fieldName;
    FilterOperator operator;
    Object value;
//    List<String> columns; https://medium.com/@sarthakagrawal.work/implementing-a-generic-jpa-filter-with-column-selector-and-pagination-using-jpa-specification-in-305ee77deae1
}
