package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.InventoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDetailRepository extends JpaRepository<InventoryDetail, Long> {
}