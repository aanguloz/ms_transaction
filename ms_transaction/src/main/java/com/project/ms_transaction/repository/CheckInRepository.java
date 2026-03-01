package com.project.ms_transaction.repository;

import com.project.ms_transaction.model.dto.CheckinListItem;
import com.project.ms_transaction.model.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckInRepository extends JpaRepository<Checkin, Long> {

    Checkin findByCode(String code);

    @Query(value = """
    
    """, nativeQuery = true)
    List<CheckinListItem> getCheckinList(@Param("filter") String filter);

}
