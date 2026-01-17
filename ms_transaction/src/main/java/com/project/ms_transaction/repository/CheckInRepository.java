package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
}
