package com.project.ms_transaction.model.dto.response;

import com.project.ms_transaction.model.dto.DocumentDTO;
import com.project.ms_transaction.model.dto.IdentifierDocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRespDTO {

    UUID id;
    String completeName;
    IdentifierDocumentDTO typeDocument;
    String numberDocument;
    String phoneNumber;
    String address;
    String email;

}
