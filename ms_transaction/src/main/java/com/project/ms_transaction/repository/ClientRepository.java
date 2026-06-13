package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.dto.response.ClientRespDTO;
import com.project.ms_transaction.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("""
    SELECT new com.project.ms_transaction.model.dto.response.ClientRespDTO(
        CASE WHEN p IS NOT NULL THEN p.completeName ELSE co.companyName END,
        CASE WHEN p IS NOT NULL THEN p.numberDocument ELSE co.rucNumber END,
        CASE WHEN p IS NOT NULL THEN p.address ELSE co.address END,
        CASE WHEN p IS NOT NULL THEN p.email ELSE co.email END,
        CASE WHEN p IS NOT NULL THEN p.phoneNumber ELSE co.phoneNumber END
    )
    FROM Client c
    LEFT JOIN c.person p
    LEFT JOIN c.company co
    WHERE (
        :filter IS NULL OR :filter = ''
        OR LOWER(p.completeName) LIKE LOWER(CONCAT('%', :filter, '%'))
        OR LOWER(p.numberDocument) LIKE LOWER(CONCAT('%', :filter, '%'))
        OR LOWER(co.companyName) LIKE LOWER(CONCAT('%', :filter, '%'))
        OR LOWER(co.rucNumber) LIKE LOWER(CONCAT('%', :filter, '%'))
    )
    """)
    List<ClientRespDTO> searchClient(@Param("filter") String filter);
}
