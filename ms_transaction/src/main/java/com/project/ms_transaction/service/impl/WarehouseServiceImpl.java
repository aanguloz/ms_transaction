package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.WareHouseReqDTO;
import com.project.ms_transaction.model.dto.response.WarehouseRespDTO;
import com.project.ms_transaction.model.entity.Warehouse;
import com.project.ms_transaction.repository.WarehouseRepository;
import com.project.ms_transaction.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Object[]> results;
        if(filter == null || filter.equals("")) {
            results = warehouseRepository.findAll().stream().map(w -> new Object[]{w.getCode(), w.getName()}).collect(Collectors.toList());
        } else
             results = warehouseRepository.search(filter);

        return results.stream()
                .map(obj -> new WarehouseRespDTO(
                        (String) obj[0], // code
                        (String) obj[1]  // name
                ))
                .collect(Collectors.toList());
    }
}
