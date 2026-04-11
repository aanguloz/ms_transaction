package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.InventoryRespDTO;

import java.util.List;

public interface InventoryService {

    List<InventoryRespDTO> inventoryList();

}
