package com.project.ms_transaction.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRespDTO {

    Long id;
    String productName;
    Integer quantity;
    Double unitCost;
    Double price;
    String location;
    String codeWarehouse;
    String warehouse;

}
