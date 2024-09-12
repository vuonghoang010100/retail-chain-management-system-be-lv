package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "bill")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bill extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    PaymentStatus paymentStatus;

    Long total;

    String note;

    @ManyToOne
    @JoinColumn(
            name = "vendor_id",
            foreignKey = @ForeignKey(name = "fk_bill_on_vendor")
    )
    Vendor vendor;

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(name = "fk_bill_on_employee")
    )
    Employee employee;

    @OneToMany
    Set<Purchase> purchases;
}
