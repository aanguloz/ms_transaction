package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.InventoryRespDTO;
import com.project.ms_transaction.repository.InventoryRepository;
import com.project.ms_transaction.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public List<InventoryRespDTO> inventoryList() {
        return inventoryRepository.getInventory().stream()
                .map(record -> new InventoryRespDTO(
                        (Long) record[0],        // pw.id
                        (String) record[1],      // pw.name -> productName
                        (Integer) record[2],     // i.quantity
                        (Double) record[3],      // (pw.price / i.quantity) as unit_cost
                        (Double) record[4],      // pw.price
                        (String) record[5],      // i."location"
                        (String) record[6],      // w.code -> codeWarehouse
                        (String) record[7]       // w."name" -> warehouse
                ))
                .toList();
    }
}
