package com.example.sales_system.repository.tenant;

import com.example.sales_system.entity.tenant.Promote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoteRepository extends JpaRepository<Promote, Long>, JpaSpecificationExecutor<Promote> {
    List<Promote> findAllByAllStore(Boolean allStore);

    List<Promote> findAllByAllProduct(Boolean allProduct);
}