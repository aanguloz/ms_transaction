package com.project.ms_transaction.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReqDTO {

    String name;
    String description;
    String location;
    String expirationDate;
    Double price;

    Long warehouseId;

}
