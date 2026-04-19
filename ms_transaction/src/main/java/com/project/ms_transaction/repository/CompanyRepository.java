package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
