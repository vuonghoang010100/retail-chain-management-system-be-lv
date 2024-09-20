package com.example.sales_system.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_use_promote")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUsePromote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(name = "fk_order_use_promote_on_order")
    )
    Order order;

    @ManyToOne
    @JoinColumn(
            name = "promote_id",
            foreignKey = @ForeignKey(name = "fk_order_use_promote_on_promote")
    )
    Promote promote;

    Long discount;
}
