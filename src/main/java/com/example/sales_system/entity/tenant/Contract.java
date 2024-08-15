package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.ContractStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    LocalDate endDate;

    @Column(nullable = false)
    Integer period;

    @Temporal(TemporalType.DATE)
    @Column(name = "latest_purchase_date")
    LocalDate latestPurchaseDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "next_purchase_date")
    LocalDate nextPurchaseDate;

    @Column(name = "pdf_url")
    String pdfUrl;

    @Enumerated(EnumType.STRING)
    ContractStatus status;

    String note;

    @ManyToOne
    @JoinColumn(
            name = "vendor_id",
            foreignKey = @ForeignKey(name = "fk_contract_on_vendor")
    )
    Vendor vendor;
}
