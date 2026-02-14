package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Boolean existsByName(String productName);

    Optional<Product> findByName(String productName);

}
