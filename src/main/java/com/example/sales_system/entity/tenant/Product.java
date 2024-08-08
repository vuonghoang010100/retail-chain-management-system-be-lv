package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String sku;

    @Column(nullable = false, unique = true)
    String name;

    String description;

    @Column(nullable = false)
    String brand;
    @Column(nullable = false)
    String unit;
    @Column(nullable = false)
    String price;

    @Enumerated(EnumType.STRING)
    ProductStatus status;
    String note;
    String image;

    @ManyToOne
    Category category;
}
