package com.project.ms_transaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {

    Long id;
    String code;
    String codeSunat;
    String name;
    String description;

}
