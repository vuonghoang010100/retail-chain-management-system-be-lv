package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "batch")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Batch extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    int quantity;

    @Column(name = "purchase_amount")
    int purchaseAmount;

    @Column(name = "purchase_price", nullable = false)
    int purchasePrice;

    @Temporal(TemporalType.DATE)
    LocalDate mfg;

    @Temporal(TemporalType.DATE)
    LocalDate exp;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            foreignKey = @ForeignKey(name = "fk_batch_on_store")
    )
    Store store;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_batch_on_product")
    )
    Product product;
}
