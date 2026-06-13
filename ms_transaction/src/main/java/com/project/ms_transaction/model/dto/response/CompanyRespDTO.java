package com.project.ms_transaction.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRespDTO {

    UUID id;
    String companyName;
    String rucNumber;
    String address;
    String email;
    String phoneNumber;

}
