package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.WareHouseReqDTO;
import com.project.ms_transaction.model.dto.response.WarehouseRespDTO;
import com.project.ms_transaction.model.entity.Warehouse;
import com.project.ms_transaction.repository.WarehouseRepository;
import com.project.ms_transaction.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public void createWareHouse(WareHouseReqDTO wareHouseReqDTO) {
        if (warehouseRepository.existsByName(wareHouseReqDTO.getName())) {
            throw new IllegalArgumentException("Warehouse with the same name already exists");
        }

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setCode(wareHouseReqDTO.getCode());
        newWarehouse.setName(wareHouseReqDTO.getName());
        newWarehouse.setDescription(wareHouseReqDTO.getDescription());

        warehouseRepository.save(newWarehouse);
    }

    @Override
    public void modifyWarehouse(Long id, WareHouseReqDTO wareHouseReqDTO) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with id: " + id));

        // Validar duplicados solo si el nombre cambió
        if (!warehouse.getName().equals(wareHouseReqDTO.getName())) {
            if (warehouseRepository.existsByName(wareHouseReqDTO.getName())) {
                throw new IllegalArgumentException("Another warehouse already uses this name");
            }
        }

        warehouse.setCode(wareHouseReqDTO.getCode());
        warehouse.setName(wareHouseReqDTO.getName());
        warehouse.setDescription(wareHouseReqDTO.getDescription());

        warehouseRepository.save(warehouse);
    }

    @Override
    public List<WarehouseRespDTO> warehouseList(String filter) {
        List<Warehouse> warehouses;
        if (filter != null && !filter.trim().isEmpty()) {
            warehouses = warehouseRepository.search(filter.trim());
        } else {
            warehouses = warehouseRepository.findAll();
        }

        return warehouses.stream()
                .map(wh -> new WarehouseRespDTO(
                        wh.getCode(),
                        wh.getName()
                ))
                .toList();
    }
}
