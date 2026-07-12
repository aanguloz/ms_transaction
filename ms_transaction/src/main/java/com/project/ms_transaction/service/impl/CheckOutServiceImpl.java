package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.*;
import com.project.ms_transaction.model.dto.request.CheckOutReqDTO;
import com.project.ms_transaction.model.dto.response.CheckOutRespDTO;
import com.project.ms_transaction.model.entity.*;
import com.project.ms_transaction.model.enums.MovementType;
import com.project.ms_transaction.model.mapper.CheckOutMapper;
import com.project.ms_transaction.repository.*;
import com.project.ms_transaction.service.CheckOutService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl implements CheckOutService {

    private final CheckoutRepository checkoutRepository;
    private final ProductWarehouseRepository productWarehouseRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final MovementRepository movementRepository;
    private final MovementDetailRepository movementDetailRepository;
    private final ClientRepository clientRepository;
    private final CheckOutMapper mapper;

    @Override
    @Transactional
    public CheckOutRespDTO addCheckOut(CheckOutReqDTO dto) {
        Checkout checkout = initializeCheckout(dto);

        Map<Long, ProductWarehouse> productWarehouseMap = loadProductWarehouses(dto);
        Map<Long, Warehouse> warehouseMap = loadWarehouses(dto);

        List<MovementDetail> movementDetails = processCheckoutItems(
                checkout, dto.getItems(), productWarehouseMap, warehouseMap
        );

        Checkout saved = checkoutRepository.save(checkout);
        registerMovementWithDetails(saved, movementDetails);

        return mapper.toRespDTO(saved);
    }

    private Checkout initializeCheckout(CheckOutReqDTO dto) {
        Checkout checkout = mapper.toEntity(dto);
        checkout.setCreatedAt(Instant.now());
        return checkout;
    }

    private Map<Long, ProductWarehouse> loadProductWarehouses(CheckOutReqDTO dto) {
        List<Long> ids = dto.getItems().stream()
                .map(CheckOutItemDTO::getProductWarehouseId)
                .distinct()
                .toList();

        return productWarehouseRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(ProductWarehouse::getId, pw -> pw));
    }

    private Map<Long, Warehouse> loadWarehouses(CheckOutReqDTO dto) {
        List<Long> ids = dto.getItems().stream()
                .map(CheckOutItemDTO::getWarehouseId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return warehouseRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Warehouse::getId, w -> w));
    }

    private List<MovementDetail> processCheckoutItems(
            Checkout checkout,
            List<CheckOutItemDTO> items,
            Map<Long, ProductWarehouse> productWarehouseMap,
            Map<Long, Warehouse> warehouseMap) {

        List<MovementDetail> movementDetails = new ArrayList<>();

        for (CheckOutItemDTO item : items) {
            ProductWarehouse productWarehouse = getProductWarehouse(item, productWarehouseMap);

            CheckoutDetail detail = createCheckoutDetail(checkout, productWarehouse, item);
            checkout.getDetails().add(detail);

            if (item.getWarehouseId() != null) {
                Warehouse warehouse = getWarehouse(item, warehouseMap);
                Inventory inventory = findOrCreateInventory(productWarehouse, warehouse);
                validateAndUpdateInventory(inventory, item, productWarehouse);

                MovementDetail movDetail = createMovementDetail(productWarehouse, warehouse, item, null);
                movementDetails.add(movDetail);
            }
        }

        return movementDetails;
    }

    private ProductWarehouse getProductWarehouse(CheckOutItemDTO item, Map<Long, ProductWarehouse> map) {
        ProductWarehouse pw = map.get(item.getProductWarehouseId());
        if (pw == null) {
            throw new EntityNotFoundException("ProductWarehouse not found: " + item.getProductWarehouseId());
        }
        return pw;
    }

    private Warehouse getWarehouse(CheckOutItemDTO item, Map<Long, Warehouse> map) {
        Warehouse w = map.get(item.getWarehouseId());
        if (w == null) {
            throw new EntityNotFoundException("Warehouse not found: " + item.getWarehouseId());
        }
        return w;
    }

    private CheckoutDetail createCheckoutDetail(Checkout checkout, ProductWarehouse pw, CheckOutItemDTO item) {
        CheckoutDetail detail = new CheckoutDetail();
        detail.setCheckout(checkout);
        detail.setProductWarehouse(pw);
        detail.setQuantityOuted(item.getQuantityOuted());
        detail.setLocation(item.getLocation());
        detail.setObservation(item.getObservation());
        return detail;
    }

    private void validateAndUpdateInventory(Inventory inventory, CheckOutItemDTO item, ProductWarehouse pw) {
        if (inventory.getQuantity().compareTo(item.getQuantityOuted()) < 0) {
            throw new RuntimeException(String.format(
                    "Stock insuficiente para %s. Disponible: %d, Solicitado: %d",
                    pw.getName(),
                    inventory.getQuantity(),
                    item.getQuantityOuted()
            ));
        }

        inventory.setQuantity(inventory.getQuantity() - (item.getQuantityOuted()));
        inventory.setLocation(item.getLocation() != null ? item.getLocation() : inventory.getLocation());
        inventory.setUpdateDate(Instant.now());
    }

    private Inventory findOrCreateInventory(ProductWarehouse productWarehouse, Warehouse warehouse) {
        return inventoryRepository
                .findByProductWarehouseAndWarehouse(productWarehouse, warehouse)
                .orElseGet(() -> {
                    Inventory newInv = Inventory.builder()
                            .productWarehouse(productWarehouse)
                            .warehouse(warehouse)
                            .quantity(0)
                            .build();
                    return inventoryRepository.save(newInv);
                });
    }

    private MovementDetail createMovementDetail(ProductWarehouse productWarehouse,
                                                Warehouse warehouse,
                                                CheckOutItemDTO item,
                                                Movement movement) {
        return MovementDetail.builder()
                .movement(movement)
                .warehouse(warehouse)
                .productWarehouse(productWarehouse)
                .productLocation(item.getLocation())
                .quantity(item.getQuantityOuted())
                .unitCost(productWarehouse.getPrice())
                .unitPrice(productWarehouse.getPrice())
                .expirationDate(productWarehouse.getExpirationDate())
                .build();
    }

    private void registerMovementWithDetails(Checkout checkout, List<MovementDetail> details) {
        if (details.isEmpty()) {
            return;
        }

        Map<Warehouse, List<MovementDetail>> detailsByWarehouse = details.stream()
                .collect(Collectors.groupingBy(MovementDetail::getWarehouse));

        for (Map.Entry<Warehouse, List<MovementDetail>> entry : detailsByWarehouse.entrySet()) {
            Warehouse warehouseOrigin = entry.getKey();
            List<MovementDetail> warehouseDetails = entry.getValue();

            Movement movement = Movement.builder()
                    .type(MovementType.OUT)
                    .checkOut(checkout)
                    .warehouseOrigin(warehouseOrigin)
                    .documentReference(checkout.getNumberDocument())
                    .observation("Salida de múltiples productos desde " + warehouseOrigin.getName())
                    .creationDate(checkout.getCreatedAt())
                    .build();

            Movement savedMovement = movementRepository.save(movement);

            warehouseDetails.forEach(detail -> {
                detail.setMovement(savedMovement);
                movementDetailRepository.save(detail);
            });
        }
    }

    @Override
    public CheckOutListItem modifyCheckOut(CheckOutReqDTO checkoutReqDTO) {
        return null;
    }

    @Override
    public List<CheckOutRespDTO> getCheckOutList(String filter) {
        // Sin Tuple, sin mapeo manual intermedio
        List<CheckOutDetailProjectionDTO> projections = checkoutRepository.listCheckOutDetails(filter);

        return projections.stream()
                .collect(Collectors.groupingBy(CheckOutDetailProjectionDTO::getId))
                .entrySet().stream()
                .map(entry -> {
                    List<CheckOutDetailProjectionDTO> rows = entry.getValue();
                    CheckOutDetailProjectionDTO first = rows.get(0);

                    List<CheckoutProductDTO> products = rows.stream()
                            .map(row -> new CheckoutProductDTO(
                                    row.getProductWarehouseId(),
                                    row.getProductName(),
                                    row.getWarehouseName(),
                                    row.getQuantity(),
                                    row.getUnitCost(),
                                    row.getUnitPrice(),
                                    row.getExpirationDate(),
                                    row.getObservation()
                            ))
                            .toList();

                    double totalValue = products.stream()
                            .mapToDouble(p -> p.getQuantity() * p.getUnitCost())
                            .sum();

                    return CheckOutRespDTO.builder()
                            .id(entry.getKey())
                            .code(first.getCode())
                            .document(new CheckoutDocumentDTO(
                                    first.getDocument(),
                                    first.getTypeDocument(),
                                    first.getNumberDocument(),
                                    null,
                                    LocalDate.now()
                            ))
                            .numberDocument(first.getNumberDocument())
                            .description(first.getDescription())
                            .clientId(first.getClient())
                            .creationDate(Instant.ofEpochSecond(first.getCreateEpoch()))
                            .products(products)
                            .totalProducts(products.size())
                            .totalValue(totalValue)
                            .build();
                })
                .toList();
    }
}
