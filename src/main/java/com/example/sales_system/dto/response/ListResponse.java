package com.example.sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListResponse<T> {
    long total;             // Number of elements
    int totalPages;         // Number of Pages
    int numOfElements;      // Number of elements in this page
    int page;
    int size;
    List<T> data;
}
