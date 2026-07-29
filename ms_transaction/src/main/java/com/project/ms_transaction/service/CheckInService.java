package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.CheckinListItem;
import com.project.ms_transaction.model.dto.request.CheckInReqDTO;
import com.project.ms_transaction.model.dto.response.CheckInRespDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CheckInService {

    CheckInRespDTO addCheckIn(CheckInReqDTO checkInReqDTO);

    CheckinListItem modifyCheckIn(CheckInReqDTO checkInReqDTO);

    List<CheckInRespDTO> getCheckinList(String filter, Pageable pageable);

}
