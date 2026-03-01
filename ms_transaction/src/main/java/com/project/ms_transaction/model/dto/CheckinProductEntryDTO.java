package com.project.ms_transaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckinProductEntryDTO {

    private Long productWarehouseId;

    // Campos adicionales para el contexto del checkin (opcional)
    private String productLocation;
    private Integer quantityReceived;
    private String observation;

}
