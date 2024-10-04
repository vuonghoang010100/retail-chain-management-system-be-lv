package com.example.sales_system.entity.tenant;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "transfer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(
            name = "from_store_id",
            foreignKey = @ForeignKey(name = "fk_transfer_on_from_store")
    )
    Store fromStore;

    @ManyToOne
    @JoinColumn(
            name = "to_store_id",
            foreignKey = @ForeignKey(name = "fk_inventory_on_to_store")
    )
    Store toStore;

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(name = "fk_transfer_on_employee")
    )
    Employee employee;

}
