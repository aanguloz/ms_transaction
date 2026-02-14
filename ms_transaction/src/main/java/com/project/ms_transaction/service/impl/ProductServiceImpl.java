package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.InventoryReqDTO;
import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;
import com.project.ms_transaction.model.entity.Inventory;
import com.project.ms_transaction.model.entity.Product;
import com.project.ms_transaction.model.entity.Warehouse;
import com.project.ms_transaction.repository.*;
import com.project.ms_transaction.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final InventoryRepository  inventoryRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public Product registerProduct(ProductReqDTO productReqDTO) {
        if (productReqDTO.getName() == null || productReqDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }

        List<Long> warehouseIds = getWarehouseIdsFromDTO(productReqDTO);
        Map<Long, Warehouse> warehouseMap = getWarehousesMap(warehouseIds);
        validateWarehousesExist(warehouseIds, warehouseMap);

        Product product = getOrCreateProduct(productReqDTO);

        mapBasicFields(product, productReqDTO);

        Set<Warehouse> warehouses = mapWarehouses(warehouseIds, warehouseMap);
        product.setWarehouses(warehouses);

        Product savedProduct = productRepository.save(product);

        if (productReqDTO.getInventories() != null && !productReqDTO.getInventories().isEmpty()) {
            List<Inventory> inventories = mapInventories(
                    productReqDTO.getInventories(),
                    savedProduct,
                    warehouseMap
            );
        }

        return savedProduct;
    }

    @Override
    public Product updateProduct(ProductReqDTO productReqDTO) {
        if (productReqDTO.getId() == null) {
            throw new IllegalArgumentException("ID del producto es requerido para actualización");
        }

        return registerProduct(productReqDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductRespDTO> listProductByWarehouse(String filter) {

        return productRepository.findAll().stream()
                .map(this::convertToRespDTO)
                .collect(Collectors.toList());
    }

    private List<Long> getWarehouseIdsFromDTO(ProductReqDTO dto) {
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

    private Product getOrCreateProduct(ProductReqDTO dto) {
        if (dto.getId() != null) {
            return productRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Producto no encontrado con ID: " + dto.getId()));
        }
        return new Product();
    }

    private void mapBasicFields(Product product, ProductReqDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setExpirationDate(dto.getExpirationDate());
        product.setPrice(dto.getPrice());
    }

    private Set<Warehouse> mapWarehouses(List<Long> warehouseIds, Map<Long, Warehouse> warehouseMap) {
        return warehouseIds.stream()
                .map(warehouseMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private List<Inventory> mapInventories(
            List<InventoryReqDTO> inventoryDTOs,
            Product product,
            Map<Long, Warehouse> warehouseMap) {

        List<Inventory> inventories = inventoryDTOs.stream()
                .map(dto -> {
                    Inventory inventory = getOrCreateInventory(dto);

                    inventory.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
                    inventory.setLocation(dto.getLocation());
                    inventory.setProduct(product);
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

        product.setInventories(inventories);

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

    private ProductRespDTO convertToRespDTO(Product product) {
        ProductRespDTO dto = new ProductRespDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setExpirationDate(product.getExpirationDate());
        dto.setPrice(product.getPrice());

        if (product.getWarehouses() != null) {
            dto.setWarehouseIds(product.getWarehouses().stream()
                    .map(Warehouse::getId)
                    .collect(Collectors.toList()));
        }

        if (product.getInventories() != null) {
            dto.setInventories(product.getInventories().stream()
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
