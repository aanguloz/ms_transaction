package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<Checkin, Long> {

    Checkin findByCode(String code);

}
