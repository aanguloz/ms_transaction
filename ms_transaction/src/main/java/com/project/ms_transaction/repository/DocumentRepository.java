package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Boolean existsByName(String name);

    Document findByName(String name);

    @Query(value = """
    select d.id, d.code, d.code_sunat, d.name, d.description, cast(d.type as text) 
    from public.document d
    where (
            :filter is null or :filter = ''
            or lower(d.code) like concat('%', lower(:filter), '%')
            or lower(d.code_sunat) like concat('%', lower(:filter), '%')
            or lower(d.name) like concat('%', lower(:filter), '%')
        )
    order by d.name asc
    """, nativeQuery = true)
    List<Object[]> listDocument(@Param("filter") String filter);

}
