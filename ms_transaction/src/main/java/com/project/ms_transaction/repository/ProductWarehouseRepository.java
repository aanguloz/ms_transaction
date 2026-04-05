package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {

    @Query(value = """
        select pw.id, pw.name, pw.description, pw.expiration_date, pw.price, w.id, w.code, w.name
        from product_warehouse_warehouse pww
        left join product_warehouse pw on pw.id = pww.product_warehouse_id
        left join warehouse w on w.id = pww.warehouse_id
           where (
               :filter = '' or :filter is null
               or lower(w.code) like lower(concat('%', :filter, '%'))
               or lower(w.name) like lower(concat('%', :filter, '%'))
               or lower(pw.name) like lower(concat('%', :filter, '%'))
           )
                   """, nativeQuery = true)
    List<Object[]> listProductByWarehouse(@Param("filter") String filter);

}
