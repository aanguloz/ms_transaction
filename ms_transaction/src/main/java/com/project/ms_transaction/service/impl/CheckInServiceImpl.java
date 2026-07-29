package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.CheckInItemDTO;
import com.project.ms_transaction.model.dto.CheckinDocumentDTO;
import com.project.ms_transaction.model.dto.CheckinListItem;
import com.project.ms_transaction.model.dto.CheckinProductDTO;
import com.project.ms_transaction.model.dto.request.CheckInReqDTO;
import com.project.ms_transaction.model.dto.response.CheckInRespDTO;
import com.project.ms_transaction.model.entity.*;
import com.project.ms_transaction.model.enums.MovementType;
import com.project.ms_transaction.model.mapper.CheckInMapper;
import com.project.ms_transaction.repository.*;
import com.project.ms_transaction.service.CheckInService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;
    private final ProductWarehouseRepository productWarehouseRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final MovementRepository movementRepository;
    private final MovementDetailRepository movementDetailRepository;
    private final CheckInMapper mapper;

    @Override
    @Transactional
    public CheckInRespDTO addCheckIn(CheckInReqDTO dto) {

        Checkin checkin = mapper.toEntity(dto);
        checkin.setCreatedAt(Instant.now());

        List<CheckinDetail> savedDetails = new ArrayList<>();
        List<MovementDetail> savedMovementDetails = new ArrayList<>();

        List<Long> pwIds = dto.getItems().stream()
                .map(CheckInItemDTO::getProductWarehouseId)
                .distinct()
                .toList();

        List<Long> wIds = dto.getItems().stream()
                .map(CheckInItemDTO::getWarehouseId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Inventory> existingInventories = inventoryRepository.findByProductWarehouseIdIn(pwIds);

        Map<Long, ProductWarehouse> pwMap = productWarehouseRepository.findAllById(pwIds)
                .stream().collect(Collectors.toMap(ProductWarehouse::getId, pw -> pw));

        Map<Long, Warehouse> wMap = warehouseRepository.findAllById(wIds)
                .stream().collect(Collectors.toMap(Warehouse::getId, w -> w));

        Map<Long, Map<Long, Inventory>> invMap = new HashMap<>();
        for (Inventory inv : existingInventories) {
            invMap.computeIfAbsent(inv.getProductWarehouse().getId(), k -> new HashMap<>())
                    .put(inv.getWarehouse().getId(), inv);
        }

        for (CheckInItemDTO item : dto.getItems()) {
            ProductWarehouse productWarehouse = pwMap.get(item.getProductWarehouseId());
            if (productWarehouse == null) {
                throw new EntityNotFoundException("ProductWarehouse not found: " + item.getProductWarehouseId());
            }

            CheckinDetail detail = new CheckinDetail();
            detail.setCheckin(checkin);
            detail.setProductWarehouse(productWarehouse);
            detail.setQuantityReceived(item.getQuantityReceived());
            detail.setLocation(item.getLocation());
            detail.setObservation(item.getObservation());

            checkin.getDetails().add(detail);
            savedDetails.add(detail);

            if (item.getWarehouseId() != null) {
                Warehouse warehouse = wMap.get(item.getWarehouseId());
                if (warehouse == null) {
                    throw new EntityNotFoundException("Warehouse not found: " + item.getWarehouseId());
                }

                Long pwId = productWarehouse.getId();
                Long wId = warehouse.getId();

                // Búsqueda o creación segura y 100% tipada (Sin Strings)
                Inventory inventory = invMap.computeIfAbsent(pwId, k -> new HashMap<>())
                        .computeIfAbsent(wId, k -> Inventory.builder()
                                .productWarehouse(productWarehouse)
                                .warehouse(warehouse)
                                .quantity(0)
                                .updateDate(Instant.now())
                                .build());

                // Acumulación segura de cantidad
                inventory.setQuantity(inventory.getQuantity() + item.getQuantityReceived());

                if (item.getLocation() != null) {
                    inventory.setLocation(item.getLocation());
                }
                inventory.setUpdateDate(Instant.now());
                savedMovementDetails.add(createMovementDetail(productWarehouse, warehouse, item, null));
            }
        }

        Checkin savedCheckin = checkInRepository.save(checkin);
        List<Inventory> inventoriesToSave = invMap.values().stream()
                .flatMap(innerMap -> innerMap.values().stream())
                .toList();

        if (!inventoriesToSave.isEmpty()) {
            inventoryRepository.saveAll(inventoriesToSave);
        }

        registerMovementWithDetails(savedCheckin, savedMovementDetails);

        return mapper.toRespDTO(savedCheckin);
    }

    private void registerMovementWithDetails(Checkin checkin, List<MovementDetail> details) {
        if (details.isEmpty()) {
            return;
        }
//
//        // 🎯 Determinar warehouse destino (del primer detalle)
//        Warehouse warehouseDestiny = details.get(0).getWarehouse();
//
//        Movement movement = Movement.builder()
//                .type(MovementType.IN)
//                .checkIn(checkin)
//                .checkOut(null)
//                .warehouseOrigin(null)
//                .warehouseDestiny(warehouseDestiny)
//                .documentReference(checkin.getNumberDocument())
//                .observation(details.get(0).getProductLocation()) // o una observación general
//                .creationDate(checkin.getCreatedAt())
//                .build();
//
//        Movement savedMovement = movementRepository.save(movement);
//
//        // 🔗 Asociar movement a cada detail y guardar
//        for (MovementDetail detail : details) {
//            detail.setMovement(savedMovement);
//            // 👇 Establecer valores desde ProductWarehouse si es necesario
//            if (detail.getUnitCost() == null) {
//                detail.setUnitCost(detail.getProductWarehouse().getPrice());
//            }
//            if (detail.getUnitPrice() == null) {
//                detail.setUnitPrice(detail.getProductWarehouse().getPrice());
//            }
//            movementDetailRepository.save(detail);
//        }
        Map<Warehouse, List<MovementDetail>> detailsByWarehouse = details.stream()
                .collect(Collectors.groupingBy(MovementDetail::getWarehouse));

        List<Movement> movementToSave = new ArrayList<>();
        List<MovementDetail> movementDetailsToSave = new ArrayList<>();

        for (Map.Entry<Warehouse, List<MovementDetail>> entry : detailsByWarehouse.entrySet()) {
            Warehouse warehouse = entry.getKey();
            List<MovementDetail> warehouseDetails = entry.getValue();

            Movement movement = Movement.builder()
                    .type(MovementType.IN)
                    .checkIn(checkin)
                    .checkOut(null)
                    .warehouseOrigin(null)
                    .warehouseDestiny(warehouse)
                    .documentReference(checkin.getNumberDocument())
                    .observation(warehouseDetails.get(0).getProductLocation())
                    .creationDate(checkin.getCreatedAt())
                    .build();

            movementToSave.add(movement);

            for (MovementDetail detail : warehouseDetails) {
                detail.setMovement(movement);
                if (detail.getUnitCost() == null) {
                    detail.setUnitCost(detail.getProductWarehouse().getPrice());
                }
                if (detail.getUnitPrice() == null) {
                    detail.setUnitPrice(detail.getProductWarehouse().getPrice());
                }
                movementDetailsToSave.add(detail);
            }
        }
        movementRepository.saveAll(movementToSave);
        movementDetailRepository.saveAll(movementDetailsToSave);
    }

    private MovementDetail createMovementDetail(ProductWarehouse productWarehouse,
                                                Warehouse warehouse,
                                                CheckInItemDTO item,
                                                Movement movement) {
        return MovementDetail.builder()
                .movement(movement)
                .warehouse(warehouse)
                .productWarehouse(productWarehouse)
                .productLocation(item.getLocation())
                .quantity(item.getQuantityReceived())
                .unitCost(productWarehouse.getPrice())
                .unitPrice(productWarehouse.getPrice())
                .expirationDate(productWarehouse.getExpirationDate()) // si existe en el DTO
                .build();
    }

//    private Inventory findOrCreateInventory(ProductWarehouse productWarehouse, Warehouse warehouse, List<Inventory> newInventoriesToSave) {
//        return inventoryRepository.findByProductWarehouseAndWarehouse(productWarehouse, warehouse)
//                .orElseGet(() -> {
//                    Inventory newInv = Inventory.builder()
//                            .productWarehouse(productWarehouse)
//                            .warehouse(warehouse)
//                            .quantity(0)
//                            .updateDate(Instant.now())
//                            .build();
//                    newInventoriesToSave.add(newInv); // Se guardará al final con saveAll
//                    return newInv;
//                });
//    }

    @Override
    public CheckinListItem modifyCheckIn(CheckInReqDTO checkInReqDTO) {
        return null;
    }

    @Override
    public List<CheckInRespDTO> getCheckinList(String filter, Pageable  pageable) {
        List<Tuple> results = checkInRepository.listCheckInDetails(filter,  pageable);

        Map<Long, List<Tuple>> groupedByCheckin = results.stream()
                .collect(Collectors.groupingBy(tuple -> tuple.get("id", Long.class)));

        return groupedByCheckin.entrySet().stream()
                .map(entry -> {
                    Long checkinId = entry.getKey();
                    List<Tuple> checkinRows = entry.getValue();
                    Tuple firstRow = checkinRows.get(0);

                    List<CheckinProductDTO> products = checkinRows.stream()
                            .map(row -> new CheckinProductDTO(
                                    row.get("product_warehouse_id", Long.class),
                                    row.get("product_name", String.class),
                                    row.get("warehouse_name", String.class),
                                    row.get("quantity", Integer.class),
                                    row.get("unit_cost", Double.class),
                                    row.get("unit_price", Double.class),
                                    row.get("expiration_date", String.class),
                                    row.get("observation", String.class)
                            ))
                            .toList();

                    BigDecimal totalValue = products.stream()
                            .map(p -> BigDecimal.valueOf(p.getQuantity())
                                    .multiply(BigDecimal.valueOf(p.getUnitCost())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return CheckInRespDTO.builder()
                            .id(firstRow.get("id", Long.class))
                            .code(firstRow.get("code", String.class))
                            .numberDocument(firstRow.get("number_document", String.class))
                            .description(firstRow.get("description", String.class))
                            .creationDate(Instant.ofEpochSecond(firstRow.get("created_at_epoch", Long.class)))
                            .document(CheckinDocumentDTO.builder()
                                    .id(firstRow.get("document", Long.class))
                                    .documentType(firstRow.get("type", String.class))
                                    .documentNumber(firstRow.get("number_document", String.class))
                                    .build())
                            .products(products)
                            .totalProducts(products.size())
                            .totalValue(totalValue)
                            .build();
                })
                .toList();
    }
}
