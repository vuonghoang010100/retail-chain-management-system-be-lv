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
}
