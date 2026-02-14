package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.InventoryReqDTO;
import com.project.ms_transaction.model.dto.request.ProductWarehouseReqDTO;
import com.project.ms_transaction.model.dto.response.ProductWarehouseRespDTO;
import com.project.ms_transaction.model.entity.Inventory;
import com.project.ms_transaction.model.entity.ProductWarehouse;
import com.project.ms_transaction.model.entity.Warehouse;
import com.project.ms_transaction.repository.*;
import com.project.ms_transaction.service.ProductWarehouseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductWarehouseServiceImpl implements ProductWarehouseService {
    private final ProductWarehouseRepository productWarehouseRepository;
    private final InventoryRepository  inventoryRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public ProductWarehouse registerProduct(ProductWarehouseReqDTO productWarehouseReqDTO) {
        if (productWarehouseReqDTO.getName() == null || productWarehouseReqDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }

        List<Long> warehouseIds = getWarehouseIdsFromDTO(productWarehouseReqDTO);
        Map<Long, Warehouse> warehouseMap = getWarehousesMap(warehouseIds);
        validateWarehousesExist(warehouseIds, warehouseMap);

        ProductWarehouse productWarehouse = getOrCreateProduct(productWarehouseReqDTO);

        mapBasicFields(productWarehouse, productWarehouseReqDTO);

        Set<Warehouse> warehouses = mapWarehouses(warehouseIds, warehouseMap);
        productWarehouse.setWarehouses(warehouses);

        ProductWarehouse savedProductWarehouse = productWarehouseRepository.save(productWarehouse);

        if (productWarehouseReqDTO.getInventories() != null && !productWarehouseReqDTO.getInventories().isEmpty()) {
            List<Inventory> inventories = mapInventories(
                    productWarehouseReqDTO.getInventories(),
                    savedProductWarehouse,
                    warehouseMap
            );
        }

        return savedProductWarehouse;
    }

    @Override
    public ProductWarehouse updateProduct(ProductWarehouseReqDTO productWarehouseReqDTO) {
        if (productWarehouseReqDTO.getId() == null) {
            throw new IllegalArgumentException("ID del producto es requerido para actualización");
        }

        return registerProduct(productWarehouseReqDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductWarehouseRespDTO> listProductByWarehouse(String filter) {

        return productWarehouseRepository.findAll().stream()
                .map(this::convertToRespDTO)
                .collect(Collectors.toList());
    }

    private List<Long> getWarehouseIdsFromDTO(ProductWarehouseReqDTO dto) {
        Set<Long> warehouseIds = new HashSet<>();

        if (dto.getWarehouseIds() != null) {
            warehouseIds.addAll(dto.getWarehouseIds());
        }

        if (dto.getInventories() != null) {
            dto.getInventories().stream()
                    .map(InventoryReqDTO::getWarehouseId)
                    .filter(Objects::nonNull)
                    .forEach(warehouseIds::add);
        }

        return new ArrayList<>(warehouseIds);
    }

    private ProductWarehouse getOrCreateProduct(ProductWarehouseReqDTO dto) {
        if (dto.getId() != null) {
            return productWarehouseRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Producto no encontrado con ID: " + dto.getId()));
        }
        return new ProductWarehouse();
    }

    private void mapBasicFields(ProductWarehouse productWarehouse, ProductWarehouseReqDTO dto) {
        productWarehouse.setName(dto.getName());
        productWarehouse.setDescription(dto.getDescription());
        productWarehouse.setExpirationDate(dto.getExpirationDate());
        productWarehouse.setPrice(dto.getPrice());
    }

    private Set<Warehouse> mapWarehouses(List<Long> warehouseIds, Map<Long, Warehouse> warehouseMap) {
        return warehouseIds.stream()
                .map(warehouseMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private List<Inventory> mapInventories(
            List<InventoryReqDTO> inventoryDTOs,
            ProductWarehouse productWarehouse,
            Map<Long, Warehouse> warehouseMap) {

        List<Inventory> inventories = inventoryDTOs.stream()
                .map(dto -> {
                    Inventory inventory = getOrCreateInventory(dto);

                    inventory.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
                    inventory.setLocation(dto.getLocation());
                    inventory.setProductWarehouse(productWarehouse);
                    inventory.setUpdateDate(new Date().toInstant());

                    if (dto.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseMap.get(dto.getWarehouseId());
                        if (warehouse == null) {
                            throw new EntityNotFoundException(
                                    "Warehouse no encontrado: " + dto.getWarehouseId());
                        }
                        inventory.setWarehouse(warehouse);
                    }

                    return inventory;
                })
                .collect(Collectors.toList());

        productWarehouse.setInventories(inventories);

        return inventories;
    }

    private Inventory getOrCreateInventory(InventoryReqDTO dto) {
        if (dto.getId() != null) {
            return inventoryRepository.findById(dto.getId())
                    .orElseGet(Inventory::new);
        }
        return new Inventory();
    }

    private Map<Long, Warehouse> getWarehousesMap(List<Long> warehouseIds) {
        if (warehouseIds.isEmpty()) {
            return new HashMap<>();
        }

        return warehouseRepository.findAllById(warehouseIds).stream()
                .collect(Collectors.toMap(Warehouse::getId, Function.identity()));
    }

    private void validateWarehousesExist(List<Long> warehouseIds, Map<Long, Warehouse> warehouseMap) {
        for (Long warehouseId : warehouseIds) {
            if (!warehouseMap.containsKey(warehouseId)) {
                throw new EntityNotFoundException(
                        "Warehouse no encontrado con ID: " + warehouseId);
            }
        }
    }

    private ProductWarehouseRespDTO convertToRespDTO(ProductWarehouse productWarehouse) {
        ProductWarehouseRespDTO dto = new ProductWarehouseRespDTO();
        dto.setId(productWarehouse.getId());
        dto.setName(productWarehouse.getName());
        dto.setDescription(productWarehouse.getDescription());
        dto.setExpirationDate(productWarehouse.getExpirationDate());
        dto.setPrice(productWarehouse.getPrice());

        if (productWarehouse.getWarehouses() != null) {
            dto.setWarehouseIds(productWarehouse.getWarehouses().stream()
                    .map(Warehouse::getId)
                    .collect(Collectors.toList()));
        }

        if (productWarehouse.getInventories() != null) {
            dto.setInventories(productWarehouse.getInventories().stream()
                    .map(this::convertInventoryToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private InventoryReqDTO convertInventoryToDTO(Inventory inventory) {
        InventoryReqDTO dto = new InventoryReqDTO();
        dto.setId(inventory.getId());
        dto.setQuantity(inventory.getQuantity());
        dto.setLocation(inventory.getLocation());
        dto.setWarehouseId(inventory.getWarehouse() != null
                ? inventory.getWarehouse().getId() : null);
        return dto;
    }
}
