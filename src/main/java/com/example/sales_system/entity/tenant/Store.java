package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "store")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Store extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(name = "full_name", nullable = false, unique = true)
    String fullName;

    @Column(nullable = false)
    String province;

    @Column(nullable = false)
    String district;

    @Column(nullable = false)
    String address;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String phone;

    @Enumerated(EnumType.STRING)
    StoreStatus status;

    String note;

    @ManyToMany(mappedBy = "stores")
    Set<Employee> employees;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Store)) return false;
        return Objects.equals(this.id, ((Store) obj).id);
    }
}
