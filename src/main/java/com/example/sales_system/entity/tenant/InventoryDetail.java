package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "inventory_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryDetail extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "real_quantity")
    Long realQuantity;

    @Column(name = "db_quantity")
    Long dbQuantity;

    @Column(name = "difference_quantity")
    Long differenceQuantity;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_inventory_detail_on_product")
    )
    Product product;

    @ManyToOne
    @JoinColumn(
            name = "batch_id",
            foreignKey = @ForeignKey(name = "fk_inventory_detail_on_batch")
    )
    Batch batch;

    @ManyToOne
    @JoinColumn(
            name = "inventory_id",
            foreignKey = @ForeignKey(name = "fk_inventory_detail_on_inventory")
    )
    Inventory inventory;
}
