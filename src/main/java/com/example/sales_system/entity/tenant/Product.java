package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product")
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

    @Column(nullable = false)
    String brand;

    @Column(nullable = false)
    String unit;

    @Column(nullable = false)
    Long price;

    String description;

    @Enumerated(EnumType.STRING)
    ProductStatus status;
    String note;

    @Column(name = "image_url")
    String imageUrl;

    @ManyToOne
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "fk_product_category")
    )
    Category category;

    @OneToMany(mappedBy = "product")
    Set<Batch> batchs;

    @Transient
    @Builder.Default
    Long stock = 0L;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Product)) return false;
        return Objects.equals(id, ((Product) obj).id);
    }
}
