package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement,Long> {
}
