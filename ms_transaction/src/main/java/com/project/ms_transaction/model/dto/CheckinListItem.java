package com.project.ms_transaction.model.dto;

import lombok.*;

import java.time.Instant;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckinListItem {

    private Long id;
    private String code;
    private String numberDocument;
    private String description;
    private Instant creationDate; // Si tienes @CreationTimestamp en la entidad

    private DocumentSummaryDTO document;

    private Integer productsCount;

}
