package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckInRepository extends JpaRepository<Checkin, Long> {

    Checkin findByCode(String code);

    @Query(value = """

            SELECT
                 c.id,
                 c.code,
                 c.number_document,
                 c.description,
                 EXTRACT(EPOCH FROM c.created_at)::INTEGER as created_at_epoch,
                 md.product_warehouse_id,
                 pw.name as product_name,
                 w.name as warehouse_name,
                 md.quantity,
                 md.unit_cost,
                 md.unit_price,
                 md.expiration_date,
                 m.observation
             FROM public.checkin c
             INNER JOIN public.movement m ON m.checkin_id = c.id
             INNER JOIN public.movement_detail md ON md.movement_id = m.id
             INNER JOIN public.product_warehouse pw ON pw.id = md.product_warehouse_id
             INNER JOIN public.warehouse w ON w.id = md.warehouse_id
             WHERE (
                 :filter IS NULL OR :filter = ''
                 OR LOWER(c.code) LIKE LOWER(CONCAT('%', :filter, '%'))
                 OR LOWER(c.number_document) LIKE LOWER(CONCAT('%', :filter, '%'))
                 OR LOWER(pw.name) LIKE LOWER(CONCAT('%', :filter, '%'))
                 OR LOWER(w.name) LIKE LOWER(CONCAT('%', :filter, '%'))
             )
             ORDER BY c.id, pw.name
        """, nativeQuery = true)
    List<Object[]> listCheckInDetails(@Param("filter") String filter);

}
