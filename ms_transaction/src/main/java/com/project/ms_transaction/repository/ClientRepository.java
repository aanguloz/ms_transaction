package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

//    @Query(value = """
//    select new com.tu.paquete.ClientRespDTO(
//        case when c.complete_name is not null then c.complete_name else c.company_name END,
//        c.identifier,
//        c.address,
//        c.email,
//        c.phone
//    )
//    from public.client c
//    where (:filter IS NULL OR :filter = ' '\s
//        or lower(c.name) like concat('%', lower(:filter), '%')
//        or lower(c.last_name) like concat('%', lower(:filter), '%')
//        or lower(c.company_name) like concat('%', lower(:filter), '%')
//        or lower(c.identifier) like concat('%', lower(:filter), '%'))
//        )
//    """, nativeQuery = true)
//    List<Object[]> searchClient(@Param("filter") String filter);

//    Optional<Client> findByIdentifier(String identifier);

}
