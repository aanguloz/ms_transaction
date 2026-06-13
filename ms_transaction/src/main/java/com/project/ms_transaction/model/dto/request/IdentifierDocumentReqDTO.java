package com.project.ms_transaction.model.dto.request;

import com.project.ms_transaction.model.enums.IdentifierType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifierDocumentReqDTO {

    String code;
    String name;
    String description;
    IdentifierType type;

}
