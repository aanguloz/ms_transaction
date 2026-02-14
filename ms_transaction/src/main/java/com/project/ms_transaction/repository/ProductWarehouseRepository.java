package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {
}
