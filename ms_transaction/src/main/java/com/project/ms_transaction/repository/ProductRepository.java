package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
