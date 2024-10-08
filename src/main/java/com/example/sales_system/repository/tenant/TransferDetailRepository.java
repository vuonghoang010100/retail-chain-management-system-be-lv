package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.TransferDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransferDetailRepository extends JpaRepository<TransferDetail, Long>, JpaSpecificationExecutor<TransferDetail> {
}