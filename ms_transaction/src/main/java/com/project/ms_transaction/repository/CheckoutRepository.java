package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
}
