package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    Boolean existsByCompleteName(String name);

    Boolean existsByNumberDocument(String numberDocument);

    Person findByNumberDocument(String numberDocument);

    @Query(value = """
        select
           p.id,
           p.complete_name,
           d.name,   
           p.number_document,
           p.phone_number,
           p.address,
           p.email
       from public.person p 
       inner join public.identifier_document d on d.id = p.identifier_document_id 
       where (
           :filter is null or :filter = ''
           or lower(p.complete_name) like concat('%', :filter, '%')
           or p.number_document like concat('%', :filter, '%')
       ) 
    """, nativeQuery = true)
    List<Object[]> findAllPerson(@Param("filter") String filter);

}
