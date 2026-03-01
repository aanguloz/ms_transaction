package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.CheckinListItem;
import com.project.ms_transaction.model.dto.request.CheckInReqDTO;

import java.util.List;

public interface CheckInService {

    CheckinListItem addCheckIn(CheckInReqDTO checkInReqDTO);

    CheckinListItem modifyCheckIn(CheckInReqDTO checkInReqDTO);

    List<CheckinListItem> getCheckinList(String filter);

}
