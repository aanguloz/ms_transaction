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
    select p.id, p.name , w.id , w.code, w.name
    from public.product_warehouse pw
    left join public.product p on p.id = pw.product_id
    left join public.warehouse w on w.id = pw.warehouse_id
    where (
        :filter is null or :filter = ''
        or lower(w.code) like lower(concat('%', :filter, '%') )
        or lower(w.name) like lower(concat('%', :filter, '%') )
        or lower(p.name) like lower(concat('%', :filter, '%') )
    )
    """, nativeQuery = true)
    List<Warehouse> search(@Param("filter") String filter);

    @Query("SELECT w FROM Warehouse w WHERE w.id IN :ids")
    List<Warehouse> findByIds(@Param("ids") List<Long> ids);

}
