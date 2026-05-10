package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.CheckOutListItem;
import com.project.ms_transaction.model.dto.request.CheckOutReqDTO;
import com.project.ms_transaction.model.dto.response.CheckOutRespDTO;

import java.util.List;

public interface CheckOutService {

    CheckOutRespDTO addCheckOut(CheckOutReqDTO checkOutReqDTO);

    CheckOutListItem modifyCheckOut(CheckOutReqDTO checkoutReqDTO);

    List<CheckOutRespDTO> getCheckOutList(String filter);

}
