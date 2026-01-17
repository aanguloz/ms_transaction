package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.WareHouseReqDTO;
import com.project.ms_transaction.model.dto.response.WarehouseRespDTO;

import java.util.List;

public interface WarehouseService {

    void createWareHouse(WareHouseReqDTO wareHouseReqDTO);

    void modifyWarehouse(WareHouseReqDTO wareHouseReqDTO);

    List<WarehouseRespDTO> warehouseList(String filter);

}
