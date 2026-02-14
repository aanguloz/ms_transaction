package com.project.ms_transaction.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReqDTO {

    private Long id; // Para actualización
    private Integer quantity;
    private String location;
    private Long warehouseId;

}
