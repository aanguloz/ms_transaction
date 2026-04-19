package com.project.ms_transaction.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRespDTO {

    String name;
    String identifier;
    String address;
    String email;
    String phone;

}
