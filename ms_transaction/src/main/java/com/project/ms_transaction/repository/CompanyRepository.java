package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Boolean existsByCompanyName(String name);

    Boolean existsByRucNumber(String rucNumber);

    Company findByRucNumber(String rucNumber);

    @Query(value = """
        select 
            c.id,
            c.company_name,
            c.ruc_number,
            c.address,
            c.email,
            c.phone_number
        from public.company c
        where (
            :filter is null or :filter = ''
            or lower(c.company_name) like concat('%', lower(:filter), '%')
            or lower(c.ruc_number) like concat('%', lower(:filter), '%')
        )
    """, nativeQuery = true)
    List<Object[]> getCompanies(@Param("filter") String filter);

}
