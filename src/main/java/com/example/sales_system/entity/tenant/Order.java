package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long tax;

    @Column(name = "tax_percentage")
    Long taxPercentage;

    @Column(name = "sub_total")
    Long subTotal;

    Long discount;

    @Column(name = "pre_tax")
    Long preTax;

    Long total;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(name = "fk_order_on_employee")
    )
    Employee employee;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            foreignKey = @ForeignKey(name = "fk_order_on_store")
    )
    Store store;

    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            foreignKey = @ForeignKey(name = "fk_order_on_customer")
    )
    Customer customer;

    @OneToOne(mappedBy = "order")
    private Invoice invoice;

//    @ManyToMany
//    @JoinTable(
//            name = "order_use_promote",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "promote_id"),
//            foreignKey = @ForeignKey(name = "order_use_promote_on_order"),
//            inverseForeignKey = @ForeignKey(name = "order_use_promote_on_promote")
//    )
//    Set<Promote> promotes;
    @OneToMany
    Set<OrderUsePromote> usePromotes;

    @OneToMany(mappedBy = "order")
    Set<OrderDetail> details;

}
