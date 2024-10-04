package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "full_name", nullable = false, unique = true)
    String fullName;

    @Temporal(TemporalType.DATE)
    LocalDate dob;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(unique = true)
    String email;

    @Column(unique = true, nullable = false)
    String phone;

    String address;
    String province;
    String district;

    Long rewardPoint;
    String note;
}
