package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.dto.projection.CheckOutDetailProjectionDTO;
import com.project.ms_transaction.model.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

//    @Query(value = """
//       select
//          c.id as id,
//          c.code as code,
//          c.number_document as numberDocument,
//          c.description as description,
//          cast(extract(epoch from c.created_at) as bigint) as createEpoch,
//          md.product_warehouse_id as productWarehouseId,
//          pw.name as productName,
//          w.name as warehouseName,
//          cast(md.quantity as integer) as quantity,
//          cast(md.unit_cost as double precision) as unitCost,
//          cast(md.unit_price as double precision) as unitPrice,
//          cast(md.expiration_date as text) as expirationDate,
//          m.observation as observation
//        from public.checkout c
//        inner join public.movement m on m.checkout_id = c.id
//        inner join public.movement_detail md on md.movement_id = m.id
//        inner join public.product_warehouse pw on pw.id = md.product_warehouse_id
//        inner join public.warehouse w on w.id = md.warehouse_id
//        where (
//            :filter is null or :filter = ''
//            or lower(c.code) like lower(concat('%', :filter, '%'))
//            or lower(c.number_document) like lower(concat('%', :filter, '%'))
//            or lower(pw.name) like lower(concat('%', :filter, '%'))
//            or lower(w.name) like lower(concat('%', :filter, '%'))
//        )
//        order by c.id, pw.name
//       """, nativeQuery = true)
//    List<Tuple> listCheckOutDetails(@Param("filter") String filter);

    @Query(value = """
    select 
        c.id as id,
        c.code as code,
        cl.id as client,
        pe.id as person,
        co.id as company,
        d.id as document,
        d.type as typeDocument,
        c.number_document as "numberDocument",
        c.description as description,
        cast(extract(epoch from c.created_at) as bigint) as "createEpoch",
        md.product_warehouse_id as "productWarehouseId",
        pw.name as "productName",
        w.name as "warehouseName",
        cast(md.quantity as integer) as quantity,
        cast(md.unit_cost as double precision) as "unitCost",
        cast(md.unit_price as double precision) as "unitPrice",
        cast(md.expiration_date as text) as "expirationDate",
        m.observation as observation
    from public.checkout c
    inner join public.movement m on m.checkout_id = c.id
    inner join public.movement_detail md on md.movement_id = m.id
    inner join public.product_warehouse pw on pw.id = md.product_warehouse_id
    inner join public.warehouse w on w.id = md.warehouse_id
    inner join public.client cl on cl.id = c.client_id
    inner join public.document d on d.id = c.document_id
    left join public.person pe on pe.id = cl.person_id
    left join public.company co on co.id = cl.company_id
    where (
        :filter is null or :filter = ''
        or lower(c.code) like lower(concat('%', :filter, '%'))
        or lower(c.number_document) like lower(concat('%', :filter, '%'))
        or lower(pw.name) like lower(concat('%', :filter, '%'))
        or lower(w.name) like lower(concat('%', :filter, '%'))
    )
    order by c.id, pw.name
""", nativeQuery = true)
    List<CheckOutDetailProjectionDTO> listCheckOutDetails(@Param("filter") String filter);
}
