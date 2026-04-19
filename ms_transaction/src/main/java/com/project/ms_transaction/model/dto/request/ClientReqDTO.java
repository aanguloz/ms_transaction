package com.project.ms_transaction.model.dto.request;

import com.project.ms_transaction.model.dto.CompanyDTO;
import com.project.ms_transaction.model.dto.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientReqDTO {

    PersonDTO person;
    CompanyDTO company;

}
