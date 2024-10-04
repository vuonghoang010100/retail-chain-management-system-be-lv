package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long quantity;
    Long price;

    @Column(name = "sub_total")
    Long subTotal;

//    Long total; // tru km de tinh doanh thu

    Boolean done; // rp

    @Temporal(TemporalType.DATE)
    @Column(name = "sale_date")
    LocalDate saleDate; // rp

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_order_detail_on_product")
    )
    Product product;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            foreignKey = @ForeignKey(name = "fk_order_detail_on_store")
    )
    Store store;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "batch_id",
            foreignKey = @ForeignKey(name = "fk_order_detail_on_batch")
    )
    Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(name = "fk_order_detail_on_orders")
    )
    Order order;
}
