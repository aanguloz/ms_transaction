package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.CheckOutItemDTO;
import com.project.ms_transaction.model.dto.CheckOutListItem;
import com.project.ms_transaction.model.dto.CheckoutProductDTO;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final CheckOutMapper mapper;

    @Override
    @Transactional
    public CheckOutRespDTO addCheckOut(CheckOutReqDTO dto) {
        Checkout checkout = mapper.toEntity(dto);
        checkout.setCreatedAt(Instant.now());

        List<CheckoutDetail> saveDetaails = new ArrayList<>();
        List<MovementDetail> movementDetails = new ArrayList<>();

        for (CheckOutItemDTO item : dto.getItems()) {
            ProductWarehouse productWarehouse = productWarehouseRepository.findById(item.getProductWarehouseId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "ProductWarehouse not found: " + item.getProductWarehouseId()));

            CheckoutDetail detail = new CheckoutDetail();
            detail.setCheckout(checkout);
            detail.setProductWarehouse(productWarehouse);
            detail.setQuantityOuted(item.getQuantityOuted());
            detail.setLocation(item.getLocation());
            detail.setObservation(item.getObservation());

            checkout.getDetails().add(detail);
            saveDetaails.add(detail);

            if(item.getWarehouseId() != null){
               Warehouse warehouse = warehouseRepository.findById(item.getWarehouseId())
                       .orElseThrow(() -> new EntityNotFoundException(
                               "Warehouse not found: " + item.getWarehouseId()));

               Inventory inventory = findOrCreateInventory(productWarehouse, warehouse);
               inventory.setQuantity(inventory.getQuantity() - item.getQuantityOuted());
               inventory.setLocation(item.getLocation() != null ? item.getLocation() : inventory.getLocation());
               inventory.setUpdateDate(Instant.now());

               MovementDetail movDetail = createMovementDetail(
                       productWarehouse,
                       warehouse,
                       item,
                       null
               );
               movementDetails.add(movDetail);
            }
        }

        Checkout saved = checkoutRepository.save(checkout);

        registerMovementWithDetails(saved, movementDetails);

        return mapper.toRespDTO(saved);
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
                .unitCost(item.getQuantityOuted() / productWarehouse.getPrice())
                .unitPrice(productWarehouse.getPrice())
                .expirationDate(productWarehouse.getExpirationDate())
                .build();
    }

    private void registerMovementWithDetails(Checkout checkout, List<MovementDetail> details) {
        if (details.isEmpty()) {
            return;
        }

        Warehouse warehouseOrigin = details.get(0).getWarehouse();

        Movement movement = Movement.builder()
                .type(MovementType.OUT)
                .checkIn(null)
                .checkOut(checkout)
                .warehouseOrigin(warehouseOrigin)
                .warehouseDestiny(null)
                .documentReference(checkout.getNumberDocument())
                .observation(details.get(0).getProductLocation()) // o una observación general
                .creationDate(checkout.getCreatedAt())
                .build();

        Movement savedMovement = movementRepository.save(movement);

        for (MovementDetail detail : details) {
            detail.setMovement(savedMovement);

            if (detail.getUnitCost() == null) {
                detail.setUnitCost(detail.getProductWarehouse().getPrice());
            }
            if (detail.getUnitPrice() == null) {
                detail.setUnitPrice(detail.getProductWarehouse().getPrice());
            }
            movementDetailRepository.save(detail);
        }
    }

    @Override
    public CheckOutListItem modifyCheckOut(CheckOutReqDTO checkoutReqDTO) {
        return null;
    }

    @Override
    public List<CheckOutRespDTO> getCheckOutList(String filter) {
        List<Object[]> results = checkoutRepository.listCheckOutDetails(filter);

        Map<Long, List<Object[]>> groupedByCheckout = results.stream()
                .collect(Collectors.groupingBy(row -> ((Number) row[0]).longValue()));

        return groupedByCheckout.entrySet().stream()
                .map(entry -> {
                    Long checkoutId = entry.getKey();
                    List<Object[]> checkoutRows = entry.getValue();

                    Object[] firstRow = checkoutRows.get(0);

                    List<CheckoutProductDTO> products = checkoutRows.stream()
                            .map(row -> new CheckoutProductDTO(
                                    ((Number) row[5]).longValue(), // productWarehouseId
                                    (String) row[6], // productName
                                    (String) row[7], // warehouseName
                                    ((Number) row[8]).intValue(), // quantity
                                    ((Number) row[9]).doubleValue(), // unitCost
                                    ((Number) row[10]).doubleValue(), // unitPrice
                                    (String) row[11], // expirationDate
                                    (String) row[12] // observation
                            ))
                            .toList();

                    BigDecimal totalValue = products.stream()
                            .map(p -> BigDecimal.valueOf(p.getQuantity() * p.getUnitCost()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return CheckOutRespDTO.builder()
                            .id(checkoutId)
                            .code((String) firstRow[1])
                            .numberDocument((String) firstRow[2])
                            .description((String) firstRow[3])
                            .creationDate(Instant.ofEpochSecond(((Number) firstRow[4]).longValue()))
                            .products(products)
                            .totalProducts(products.size())
                            .totalValue(totalValue)
                            .build();
                })
                .toList();
    }
}
