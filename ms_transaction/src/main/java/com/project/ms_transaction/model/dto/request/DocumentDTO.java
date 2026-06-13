package com.project.ms_transaction.model.dto.request;

import com.project.ms_transaction.model.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {

    String code;
    String codeSunat;
    String name;
    String description;

    DocumentType documentType;

}
