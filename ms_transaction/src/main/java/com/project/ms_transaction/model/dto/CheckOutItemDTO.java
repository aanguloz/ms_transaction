package com.project.ms_transaction.model.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutItemDTO {

    private Long productWarehouseId;
    private Long warehouseId;
    private Integer quantityOuted;
    private String location;
    private String observation;

}
