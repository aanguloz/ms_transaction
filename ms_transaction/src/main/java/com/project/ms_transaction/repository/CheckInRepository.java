package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Checkin;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckInRepository extends JpaRepository<Checkin, Long> {

    Checkin findByCode(String code);

//    @Query(value = """
//        SELECT
//             c.id,
//             c.code,
//             d.id as document,
//             d.type as type,
//             c.number_document,
//             c.description,
//             EXTRACT(EPOCH FROM c.created_at)::BIGINT as created_at_epoch,
//             md.product_warehouse_id,
//             pw.name as product_name,
//             w.name as warehouse_name,
//             md.quantity,
//             md.unit_cost,
//             md.unit_price,
//             md.expiration_date,
//             m.observation
//         FROM public.checkin c
//         INNER JOIN public.movement m ON m.checkin_id = c.id
//         INNER JOIN public.movement_detail md ON md.movement_id = m.id
//         INNER JOIN public.product_warehouse pw ON pw.id = md.product_warehouse_id
//         INNER JOIN public.warehouse w ON w.id = md.warehouse_id
//         INNER JOIN public.document d ON d.id = c.document_id
//         WHERE (
//             :filter IS NULL OR :filter = ''
//             OR LOWER(c.code) LIKE LOWER(CONCAT('%', :filter, '%'))
//             OR LOWER(c.number_document) LIKE LOWER(CONCAT('%', :filter, '%'))
//             OR LOWER(pw.name) LIKE LOWER(CONCAT('%', :filter, '%'))
//             OR LOWER(w.name) LIKE LOWER(CONCAT('%', :filter, '%'))
//         )
//         ORDER BY c.id, pw.name
//        """, nativeQuery = true)
    @Query(value = """
        WITH FilteredCheckins AS (
            SELECT c.id FROM checkin c 
            WHERE c.code ILIKE CONCAT('%', :filter, '%')
            UNION
            SELECT c.id FROM checkin c 
            JOIN document d ON d.id = c.document_id
            WHERE c.number_document ILIKE CONCAT('%', :filter, '%')
            UNION
            SELECT c.id FROM checkin c
            WHERE EXISTS (
                SELECT 1 FROM movement m
                JOIN movement_detail md ON md.movement_id = m.id
                JOIN product_warehouse pw ON pw.id = md.product_warehouse_id
                WHERE m.checkin_id = c.id AND pw.name ILIKE CONCAT('%', :filter, '%')
            )
            UNION
            SELECT c.id FROM checkin c
            WHERE EXISTS (
                SELECT 1 FROM movement m
                JOIN movement_detail md ON md.movement_id = m.id
                JOIN warehouse w ON w.id = md.warehouse_id
                WHERE m.checkin_id = c.id AND w.name ILIKE CONCAT('%', :filter, '%')
            )
        )
        SELECT 
            c.id, c.code, d.id as document, d.type as type,
            c.number_document, c.description,
            EXTRACT(EPOCH FROM c.created_at)::BIGINT as created_at_epoch,
            md.product_warehouse_id, pw.name as product_name,
            w.name as warehouse_name, md.quantity,
            md.unit_cost, md.unit_price, md.expiration_date,
            m.observation,
            SUM(md.quantity * md.unit_cost) OVER(PARTITION BY c.id) as total_value
        FROM checkin c
        JOIN movement m ON m.checkin_id = c.id
        JOIN movement_detail md ON md.movement_id = m.id
        JOIN product_warehouse pw ON pw.id = md.product_warehouse_id
        JOIN warehouse w ON w.id = md.warehouse_id
        JOIN document d ON d.id = c.document_id
        WHERE c.id IN (SELECT id FROM FilteredCheckins)
        ORDER BY c.id, pw.name
        """,
        nativeQuery = true)
    List<Tuple> listCheckInDetails(@Param("filter") String filter, Pageable pageable);

}
