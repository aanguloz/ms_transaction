package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.CheckInItemDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<MovementDetail> movementDetails = new ArrayList<>();

        // 📦 Procesar cada ítem del request
        for (CheckInItemDTO item : dto.getItems()) {

            // 🔍 Buscar ProductWarehouse existente
            ProductWarehouse productWarehouse = productWarehouseRepository.findById(item.getProductWarehouseId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "ProductWarehouse not found: " + item.getProductWarehouseId()));

            // 🧱 Crear CheckinDetail NUEVO en cada iteración
            CheckinDetail detail = new CheckinDetail();
            detail.setCheckin(checkin);
            detail.setProductWarehouse(productWarehouse);
            detail.setQuantityReceived(item.getQuantityReceived());
            detail.setLocation(item.getLocation());
            detail.setObservation(item.getObservation());

            checkin.getDetails().add(detail);
            savedDetails.add(detail);

            // 📦 Gestionar Inventory si se proporcionó warehouseId
            if (item.getWarehouseId() != null) {
                Warehouse warehouse = warehouseRepository.findById(item.getWarehouseId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Warehouse not found: " + item.getWarehouseId()));

                Inventory inventory = findOrCreateInventory(productWarehouse, warehouse);
                inventory.setQuantity(inventory.getQuantity() + item.getQuantityReceived());
                inventory.setLocation(item.getLocation() != null ? item.getLocation() : inventory.getLocation());
                inventory.setUpdateDate(Instant.now());

                // 🔗 Crear MovementDetail para este item
                MovementDetail movDetail = createMovementDetail(
                        productWarehouse,
                        warehouse,
                        item,
                        null // Se asignará después de guardar el movement
                );
                movementDetails.add(movDetail);
            }
        }

        // 💾 Guardar Checkin (con cascade se guardan los detalles)
        Checkin saved = checkInRepository.save(checkin);

        // 📝 Registrar Movement y sus detalles
        registerMovementWithDetails(saved, movementDetails);

        return mapper.toRespDTO(saved);
    }

    private void registerMovementWithDetails(Checkin checkin, List<MovementDetail> details) {
        if (details.isEmpty()) {
            return;
        }

        // 🎯 Determinar warehouse destino (del primer detalle)
        Warehouse warehouseDestiny = details.get(0).getWarehouse();

        Movement movement = Movement.builder()
                .type(MovementType.IN)
                .checkIn(checkin)
                .checkOut(null)
                .warehouseOrigin(null)
                .warehouseDestiny(warehouseDestiny)
                .documentReference(checkin.getNumberDocument())
                .observation(details.get(0).getProductLocation()) // o una observación general
                .creationDate(checkin.getCreatedAt())
                .build();

        Movement savedMovement = movementRepository.save(movement);

        // 🔗 Asociar movement a cada detail y guardar
        for (MovementDetail detail : details) {
            detail.setMovement(savedMovement);
            // 👇 Establecer valores desde ProductWarehouse si es necesario
            if (detail.getUnitCost() == null) {
                detail.setUnitCost(detail.getProductWarehouse().getPrice());
            }
            if (detail.getUnitPrice() == null) {
                detail.setUnitPrice(detail.getProductWarehouse().getPrice());
            }
            movementDetailRepository.save(detail);
        }
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
                .unitCost(item.getQuantityReceived() / productWarehouse.getPrice())
                .unitPrice(productWarehouse.getPrice())
                .expirationDate(productWarehouse.getExpirationDate()) // si existe en el DTO
                .build();
    }

    private Inventory findOrCreateInventory(ProductWarehouse productWarehouse, Warehouse warehouse) {
        return inventoryRepository  // 👈 Repositorio correcto
                .findByProductWarehouseAndWarehouse(productWarehouse, warehouse)
                .orElseGet(() -> {
                    Inventory newInv = Inventory.builder()
                            .productWarehouse(productWarehouse)
                            .warehouse(warehouse)
                            .quantity(0)
                            .updateDate(Instant.now())
                            .build();
                    // 👇 IMPORTANTE: Guarda el nuevo inventory si lo creas
                    return inventoryRepository.save(newInv);
                });
    }

    @Override
    public CheckinListItem modifyCheckIn(CheckInReqDTO checkInReqDTO) {
        return null;
    }

    @Override
    public List<CheckInRespDTO> getCheckinList(String filter) {
        List<Object[]> results = checkInRepository.listCheckInDetails(filter);

        // Group by checkin ID
        Map<Long, List<Object[]>> groupedByCheckin = results.stream()
                .collect(Collectors.groupingBy(row -> ((Number) row[0]).longValue()));

        return groupedByCheckin.entrySet().stream()
                .map(entry -> {
                    Long checkinId = entry.getKey();
                    List<Object[]> checkinRows = entry.getValue();

                    // Get first row to extract checkin header info
                    Object[] firstRow = checkinRows.get(0);

                    // Build product list from all rows
                    List<CheckinProductDTO> products = checkinRows.stream()
                            .map(row -> new CheckinProductDTO(
                                    ((Number) row[5]).longValue(),  // product_warehouse_id
                                    (String) row[6],                 // product_name
                                    (String) row[7],                 // warehouse_name
                                    ((Number) row[8]).intValue(),    // quantity
                                    (Double) row[9],                 // unit_cost
                                    (Double) row[10],                // unit_price
                                    (String) row[11],                // expiration_date
                                    (String) row[12]                 // observation
                            ))
                            .toList();

                    // Calculate totals
                    BigDecimal totalValue = products.stream()
                            .map(p -> BigDecimal.valueOf(p.getQuantity() * p.getUnitCost()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return CheckInRespDTO.builder()
                            .id(checkinId)
                            .code((String) firstRow[1])           // code
                            .numberDocument((String) firstRow[2])  // numberDocument
                            .description((String) firstRow[3])     // description
                            .creationDate(Instant.ofEpochSecond(((Number) firstRow[4]).longValue()))
                            .products(products)
                            .totalProducts(products.size())
                            .totalValue(totalValue)
                            .build();
                })
                .toList();
    }
}
