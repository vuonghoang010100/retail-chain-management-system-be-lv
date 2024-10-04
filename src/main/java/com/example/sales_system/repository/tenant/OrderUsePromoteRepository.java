package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.OrderUsePromote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderUsePromoteRepository extends JpaRepository<OrderUsePromote, Long>, JpaSpecificationExecutor<OrderUsePromote> {
}
