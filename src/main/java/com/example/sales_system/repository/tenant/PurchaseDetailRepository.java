package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, Long>, JpaSpecificationExecutor<PurchaseDetail> {
}
