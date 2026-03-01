package com.project.ms_transaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentSummaryDTO {

    private Long id;
    private String documentType; // o lo que necesites mostrar
    private String reference;

}
