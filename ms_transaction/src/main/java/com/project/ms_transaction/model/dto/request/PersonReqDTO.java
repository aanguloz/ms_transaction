package com.project.ms_transaction.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonReqDTO {

    String completeName;
    String identifier;
    String numberIdentifier;
    String phoneNumber;
    String address;
    String email;

}
