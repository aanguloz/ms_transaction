package com.project.ms_transaction.model.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckInItemDTO {

    private Long productWarehouseId;
    private Long warehouseId; // <--- CRUCIAL: Faltaba en tu DTO original para crear el Inventory
    private Integer quantityReceived;
    private String location;
    private String observation;

}
