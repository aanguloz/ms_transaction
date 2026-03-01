package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.CheckinListItem;
import com.project.ms_transaction.model.dto.request.CheckInReqDTO;
import com.project.ms_transaction.repository.CheckInRepository;
import com.project.ms_transaction.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;

    @Override
    public CheckinListItem addCheckIn(CheckInReqDTO checkInReqDTO) {
        return null;
    }

    @Override
    public CheckinListItem modifyCheckIn(CheckInReqDTO checkInReqDTO) {
        return null;
    }

    @Override
    public List<CheckinListItem> getCheckinList(String filter) {
        return List.of();
    }
}
