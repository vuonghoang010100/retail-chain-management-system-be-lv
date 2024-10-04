package com.example.sales_system.repository.master;

import com.example.sales_system.entity.master.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LookupRepository extends JpaRepository<Lookup, Long> {
  Lookup findLookupByName(String tenant);
}