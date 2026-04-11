package com.project.ms_transaction.model.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckinProductDTO {

    private Long productWarehouseId;
//    private Long productId;
//    private String productCode;
    private String productName;
    private String warehouseName;
//    private String productLocation;
    private Integer quantity;
    private Double unitCost;
    private Double unitPrice;
    private String expirationDate;
    private String observation; // del checkin_products

}
