package com.project.ms_transaction.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutDocumentDTO {

    private Long id;
    private String documentType;
    private String documentNumber;
    private String issuer;
    private LocalDate issueDate;

}
