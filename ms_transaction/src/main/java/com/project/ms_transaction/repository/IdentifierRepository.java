package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.IdentifierDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IdentifierRepository extends JpaRepository<IdentifierDocument, Long> {

    Boolean existsByName(String name);

    IdentifierDocument findByName(String name);

    @Query(value = """
        select 
           i.code,
           i,name,
           i.description,
           i.type
        from public.identifier_document i
        where (
            :filter is null or :filter = ''
            or lower(i.code) like concat('&', lower(:filter), '%')
            or lower(i.name) like concat('%', lower(:filter), '%')
            or lower(i.type) like concat('%', lower(:filter), '%')
        )
    """, nativeQuery = true)
    List<Object[]> getIdentifierDocuments(@Param("filter")String filter);

}
