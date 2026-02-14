package com.project.ms_transaction.model.dto.response;

import com.project.ms_transaction.model.dto.request.InventoryReqDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWarehouseRespDTO {

    Long id;
    String name;
    String description;
    String expirationDate;
    Double price;

    List<Long> warehouseIds;
    List<InventoryReqDTO> inventories;

}
