package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.CheckinDetail;
import com.project.ms_transaction.model.entity.Inventory;
import com.project.ms_transaction.model.entity.ProductWarehouse;
import com.project.ms_transaction.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckInDetailRepository extends JpaRepository<CheckinDetail, Long> {

    //Optional<Inventory> findInventoryByProductWarehouseAndWarehouse(ProductWarehouse productWarehouse, Warehouse warehouse);

}
