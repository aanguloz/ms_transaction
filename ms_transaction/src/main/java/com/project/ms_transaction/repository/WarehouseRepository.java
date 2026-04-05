package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Warehouse findByCode(String code);

    boolean existsByName(String name);

    @Query(value = """
    select distinct w.code, w.name
    from public.product_warehouse_warehouse pww
    left join public.product_warehouse pw on pw.id = pww.product_warehouse_id
    left join public.warehouse w on w.id = pww.warehouse_id
    where (
        :filter is null or :filter = ''
        or lower(w.code) like lower(concat('%', cast(:filter as text), '%') )
        or lower(w.name) like lower(concat('%', cast(:filter as text), '%') )
        or lower(pw.name) like lower(concat('%', cast(:filter as text), '%') )
    )
    """, nativeQuery = true)
    List<Object[]> search(@Param("filter") String filter);

    @Query("SELECT w FROM Warehouse w WHERE w.id IN :ids")
    List<Warehouse> findByIds(@Param("ids") List<Long> ids);

}
