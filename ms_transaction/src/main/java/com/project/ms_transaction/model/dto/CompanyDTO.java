package com.project.ms_transaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    private String companyName;
    private String rucNumber;
    private String address;
    private String email;
    private String phoneNumber;

}
