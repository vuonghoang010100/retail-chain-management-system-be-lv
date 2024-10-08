package com.example.sales_system.entity.tenant;

import com.example.sales_system.entity.AbstractTimestampEntity;
import com.example.sales_system.enums.EmployeeStatus;
import com.example.sales_system.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "employee")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee extends AbstractTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /* Template for Custom
    @Id
    @StringPrefixedIdentifier(
            name = "employee_id_seq",
            prefix = "NV_",
            formatNumber = "%06d"
    )
    String id;
    */

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Temporal(TemporalType.DATE)
    LocalDate dob;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(unique = true, nullable = false)
    String email;

    @Column(unique = true)
    String phone;

    @Column(nullable = false)
    String password;
    String address;
    String province;
    String district;

    @Enumerated(EnumType.STRING)
    EmployeeStatus status;
    String note;

    @ManyToMany
    @JoinTable(name = "employee_has_role", // employee_has_role
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"),
            foreignKey = @ForeignKey(name = "fk_employee_has_role_on_employee"),
            inverseForeignKey = @ForeignKey(name = "fk_employee_has_role_on_role")
    )
    Set<Role> roles;

    // store
    Boolean allStore;

    @ManyToMany
    @JoinTable(name = "employee_work_in_store",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id"),
            foreignKey = @ForeignKey(name = "fk_employee_work_in_store_on_employee"),
            inverseForeignKey = @ForeignKey(name = "fk_employee_work_in_store_on_store")
    )
    Set<Store> stores;

}
