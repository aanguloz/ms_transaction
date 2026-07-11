package com.project.ms_transaction.model.dto;

//public record CheckOutDetailProjectionDTO(
//        Long id,
//        String code,
//        String numberDocument,
//        String description,
//        Long createEpoch,
//        Long productWarehouseId,
//        String productName,
//        String warehouseName,
//        Integer quantity,
//        Double unitCost,
//        Double unitPrice,
//        String expirationDate,
//        String observation
//) {}

import com.project.ms_transaction.model.enums.DocumentType;

public interface CheckOutDetailProjectionDTO {
    Long getId();
    String getCode();
    Long getClient();
    Long getPerson();
    Long getCompany();
    Long getDocument();
    String getTypeDocument();
    String getNumberDocument();
    String getDescription();
    Long getCreateEpoch();
    Long getProductWarehouseId();
    String getProductName();
    String getWarehouseName();
    Integer getQuantity();
    Double getUnitCost();
    Double getUnitPrice();
    String getExpirationDate();
    String getObservation();
}