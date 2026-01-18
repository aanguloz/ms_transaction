package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.MovementDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementDetailRepository extends JpaRepository<MovementDetail,Long> {
}
