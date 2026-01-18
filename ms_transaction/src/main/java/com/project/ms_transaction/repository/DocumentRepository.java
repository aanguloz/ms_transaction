package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
