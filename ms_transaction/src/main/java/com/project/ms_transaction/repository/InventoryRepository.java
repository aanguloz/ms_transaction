package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Inventory;
import com.project.ms_transaction.model.entity.ProductWarehouse;
import com.project.ms_transaction.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductWarehouseAndWarehouse(
            ProductWarehouse productWarehouse,
            Warehouse warehouse
    );

    @Query(value = """
           select pw.id, pw.name, i.quantity, (pw.price / i.quantity ) as unit_cost, pw.price, i."location", w.code , w."name"
            from public.inventory i
            inner join public.product_warehouse pw on pw.id = i.product_warehouse_id
            inner join public.warehouse w on w.id = i.warehouse_id
            """, nativeQuery = true)
    List<Object[]> getInventory();
}
