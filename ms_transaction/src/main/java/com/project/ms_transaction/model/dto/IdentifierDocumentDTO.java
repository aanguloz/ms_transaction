package com.project.ms_transaction.model.dto;

import com.project.ms_transaction.model.enums.IdentifierType;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentifierDocumentDTO {

    Long id;
    String code;
    String name;
    String description;
    IdentifierType type;

}
