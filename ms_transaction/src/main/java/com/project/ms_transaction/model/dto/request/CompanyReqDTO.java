package com.project.ms_transaction.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyReqDTO {

    String companyName;
    String rucNumber;
    String address;
    String email;
    String phoneNumber;

}
