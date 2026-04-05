package com.project.ms_transaction.model.dto.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWarehouseReqDTO {

    private Long id; // Para actualización
    private String name;
    private String description;
    private String expirationDate;
    private Double price;

    private List<Long> warehouseIds;

    private List<InventoryReqDTO> inventories;

    private String documentRef; // Campo adicional si lo necesitas

}
