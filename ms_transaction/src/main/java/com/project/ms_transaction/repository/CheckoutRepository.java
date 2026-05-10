package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    @Query(value = """
        select
                c.id,
                c.code,
                c.number_docuemnt,
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
            from public.checkout c
            inner join public.movement m on m.checkout_id = c.id
            inner join public.movement_detail md on md.movement_id = m.id
            inner join public.product_warehouse pw on pw.id = md.product_warehouse_id
            inner join public.warehouse w on w.id = md.warehouse_id
            where (
                :filter is null or :filter = ''
                or lower(c.code) like lower(concat('%', :filter, '%'))
                or lower(c.number_docuemnt) like lower(concat('%', :filter, '%'))
                or lower(pw.name) like lower(concat('%', :filter, '%'))
                or lower(w.name) like lower(concat('%', :filter, '%'))
            )
            order by c.id, pw.name
                    
    """, nativeQuery = true)
    List<Object[]> listCheckOutDetails(@Param("filter") String filter);
}
