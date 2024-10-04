package com.example.sales_system.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "transfer_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long quantity;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_transfer_detail_on_product")
    )
    Product product;

    @ManyToOne
    @JoinColumn(
            name = "from_batch_id",
            foreignKey = @ForeignKey(name = "fk_transfer_detail_on_from_batch")
    )
    Batch fromBatch;

    @ManyToOne
    @JoinColumn(
            name = "to_batch_id",
            foreignKey = @ForeignKey(name = "fk_transfer_detail_on_to_batch")
    )
    Batch toBatch;

    @ManyToOne
    @JoinColumn(
            name = "transfer_id",
            foreignKey = @ForeignKey(name = "fk_transfer_detail_on_transfer")
    )
    Transfer transfer;
}
