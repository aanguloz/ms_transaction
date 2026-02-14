package com.project.ms_transaction.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReqDTO {

    private Long id; // Para actualización
    private String name;
    private String description;
    private String expirationDate;
    private Double price;

    // Relación @ManyToMany - múltiples warehouses
    private List<Long> warehouseIds;

    // Relación @OneToMany - inventarios con location y warehouse
    private List<InventoryReqDTO> inventories;

    private String documentRef; // Campo adicional si lo necesitas

}
