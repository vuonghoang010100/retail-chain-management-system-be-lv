package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "purchase_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseDetail extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Order Purchase info

    @Column(name = "purchase_price", nullable = false)
    Long purchasePrice;

    @Column(name = "purchase_amount", nullable = false)
    Long purchaseAmount;

    @Column(name = "received_amount")
    Long receivedAmount;

    @Column(name = "sub_total", nullable = false)
    Long subTotal;

    // Receive info
//    @Temporal(TemporalType.DATE)
//    @Column(name = "received_date")
//    LocalDate receivedDate;

    @Temporal(TemporalType.DATE)
    LocalDate mfg;

    @Temporal(TemporalType.DATE)
    LocalDate exp;

    // relationships
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_purchase_detail_on_product")
    )
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "purchase_id",
            foreignKey = @ForeignKey(name = "fk_purchase_detail_on_purchase")
    )
    Purchase purchase;

    @OneToOne
    @JoinColumn(
            name = "batch_id",
            foreignKey = @ForeignKey(name = "fk_purchase_detail_on_batch")
    )
    Batch batch;
}
