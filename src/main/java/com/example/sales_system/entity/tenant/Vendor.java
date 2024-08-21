package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.VendorStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "vendor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vendor extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "full_name", nullable = false, unique = true)
    String fullName;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String phone;

    String address;
    String province;
    String district;

    @Enumerated(EnumType.STRING)
    VendorStatus status;

    String note;

    @OneToMany(mappedBy = "vendor")
    private Set<Contract> contracts;
}
