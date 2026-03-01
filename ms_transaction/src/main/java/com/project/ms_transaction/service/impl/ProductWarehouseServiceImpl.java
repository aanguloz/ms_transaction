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
    public ProductWarehouse modifyProduct(ProductWarehouseReqDTO productWarehouseReqDTO) {
        if (productWarehouseReqDTO.getId() == null) {
            throw new IllegalArgumentException("ID del producto es requerido para actualización");
        }

        return registerProduct(productWarehouseReqDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductWarehouseRespDTO> listProductByWarehouse(String filter) {

        List<Object[]> results = productWarehouseRepository.listProductByWarehouse(filter);

        // Agrupar por ID de producto
        Map<Long, List<Object[]>> groupedByProduct = results.stream()
                .collect(Collectors.groupingBy(row -> (Long) row[0]));

        return groupedByProduct.entrySet().stream()
                .map(entry -> convertToRespDTO(entry.getValue()))
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

    private ProductWarehouseRespDTO convertToRespDTO(List<Object[]> productRows) {
        ProductWarehouseRespDTO dto = new ProductWarehouseRespDTO();

        // Primera fila: datos del producto
        Object[] firstRow = productRows.get(0);
        dto.setId(safeCast(firstRow[0], Long.class));
        dto.setName(safeCast(firstRow[1], String.class));
        dto.setDescription(safeCast(firstRow[2], String.class));
        dto.setExpirationDate(formatDate(firstRow[3])); // Manejo seguro de fechas
        dto.setPrice(safeCast(firstRow[4], Double.class));

        // Agrupar IDs de almacenes (columna 5 = w.id)
        List<Long> warehouseIds = productRows.stream()
                .map(row -> safeCast(row[5], Long.class))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        dto.setWarehouseIds(warehouseIds);

        // Opcional: Cargar inventarios si los necesitas
        // dto.setInventories(loadInventoriesForProduct(dto.getId()));

        return dto;
    }

    private <T> T safeCast(Object value, Class<T> type) {
        if (value == null) return null;
        if (type.isInstance(value)) return type.cast(value);

        // Conversión de Number a String si es necesario
        if (type == String.class && value instanceof Number) {
            return type.cast(value.toString());
        }
        // Conversión de String a Number si es necesario
        if (type == Double.class && value instanceof String) {
            try {
                return type.cast(Double.valueOf((String) value));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String formatDate(Object dateValue) {
        if (dateValue == null) return null;
        if (dateValue instanceof java.sql.Date) {
            return ((java.sql.Date) dateValue).toString();
        }
        if (dateValue instanceof java.time.LocalDate) {
            return ((java.time.LocalDate) dateValue).toString();
        }
        return dateValue.toString();
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
