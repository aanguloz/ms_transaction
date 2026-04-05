package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Inventory;
import com.project.ms_transaction.model.entity.ProductWarehouse;
import com.project.ms_transaction.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Optional<Inventory> findByProductWarehouseAndWarehouse(
            ProductWarehouse productWarehouse,
            Warehouse warehouse
    );

    @Query("SELECT i FROM Inventory i WHERE i.productWarehouse = :productWarehouse AND i.warehouse = :warehouse")
    Optional<Inventory> findByProductWarehouseAndWarehouseExplicit(
            @Param("productWarehouse") ProductWarehouse productWarehouse,
            @Param("warehouse") Warehouse warehouse
    );
}
