package com.project.ms_transaction.model.dto;

import com.project.ms_transaction.model.entity.Document;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private String completeName;

    private IdentifierDocumentDTO typeDocument;

    private String numberDocument;
    private String phoneNumber;
    private String address;
    private String email;

}
