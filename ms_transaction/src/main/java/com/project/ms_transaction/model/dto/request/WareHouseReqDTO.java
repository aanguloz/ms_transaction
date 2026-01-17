package com.project.ms_transaction.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WareHouseReqDTO {

    Long id;
    String code;
    String name;
    String description;

}
