package com.project.ms_transaction.model.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutProductDTO {

    private Long productWarehouseId;
    private String productName;
    private String warehouseName;
    private Integer quantity;
    private Double unitCost;
    private Double unitPrice;

    private String expirationDate;
    private String observation;

}
