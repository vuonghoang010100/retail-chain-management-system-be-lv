package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.PromoteStatus;
import com.example.sales_system.enums.PromoteType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "promote")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Promote extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    PromoteType type;

    // 1. type percentage
    Long percentage;

    @Column(name = "max_discount")
    Long maxDiscount;

    // 2. type amount
    Long amount;

    // 3. type product
    @Column(name = "discount_price")
    Long discountPrice;

    // active traits
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    LocalDate endDate;

    Long quantity;

    @Enumerated(EnumType.STRING)
    PromoteStatus status;

    // check type 1, 2
    @Column(name = "min_quantity_required")
    Long minQuantityRequired;

    @Column(name = "min_amount_required")
    Long minAmountRequired;

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(name = "fk_promote_on_employee")
    )
    Employee employee;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_promote_on_product")
    )
    Product product;

    @Column(name = "all_store")
    Boolean allStore;

    @ManyToMany
    @JoinTable(
            name = "promote_on_store",
            joinColumns = @JoinColumn(name = "promote_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id"),
            foreignKey = @ForeignKey(name = "promote_on_store_on_promote"),
            inverseForeignKey = @ForeignKey(name = "promote_on_store_on_store")
    )
    Set<Store> stores;

    public boolean isAplyAbleFor(Store store, Product product, Long quantity) {

        if (!this.stores.contains(store))
            return false;

        if (this.minQuantityRequired != null && quantity < this.minQuantityRequired)
            return false;

        // ?
        return this.minAmountRequired == null || quantity * product.getPrice() >= this.minAmountRequired;
    }
}
