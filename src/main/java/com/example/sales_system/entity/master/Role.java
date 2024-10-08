package com.example.sales_system.entity.master;

import com.example.sales_system.entity.AbstractTimestampEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends AbstractTimestampEntity {
    @Id
    String name;
    String description;
}
