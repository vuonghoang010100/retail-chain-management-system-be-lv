package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.PaymentStatus;
import com.example.sales_system.enums.PurchaseStatus;
import com.example.sales_system.enums.ReceiveStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "purchase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Purchase extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "use_contract", nullable = false)
    boolean useContract;

    @Temporal(TemporalType.DATE)
    @Column(name = "received_date")
    LocalDate receivedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PurchaseStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "receive_status", nullable = false)
    ReceiveStatus receiveStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    PaymentStatus paymentStatus;

    Long total;

    String note;

    @ManyToOne
    @JoinColumn(
            name = "vendor_id",
            foreignKey = @ForeignKey(name = "fk_purchase_on_vendor")
    )
    Vendor vendor;

    @ManyToOne
    @JoinColumn(
            name = "contract_id",
            foreignKey = @ForeignKey(name = "fk_purchase_on_contract")
    )
    Contract contract;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            foreignKey = @ForeignKey(name = "fk_purchase_on_store")
    )
    Store store;

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(name = "fk_purchase_on_employee")
    )
    Employee employee;

    @ManyToOne
    @JoinColumn(
            name = "bill_id",
            foreignKey = @ForeignKey(name = "fk_purchase_on_bill")
    )
    Bill bill;

    @OneToMany(mappedBy = "purchase")
    Set<PurchaseDetail> details;
}
