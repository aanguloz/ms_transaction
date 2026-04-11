package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Boolean existsByName(String productName);

    @Query(value = """
            select *
            from public.product p
            where p.name = :productName""", nativeQuery = true)
    Product getProductByName(String productName);

    Optional<Product> findByName(String productName);

}
