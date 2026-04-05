package com.project.ms_transaction.model.mapper;

import com.project.ms_transaction.model.dto.CheckinDocumentDTO;
import com.project.ms_transaction.model.dto.DocumentDTO;
import com.project.ms_transaction.model.dto.DocumentSummaryDTO;
import com.project.ms_transaction.model.entity.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentDTO toDocumentDTO(Document document) {
        if (document == null) {return null;}

        return DocumentDTO.builder()
                .id(document.getId())
                .code(document.getCode())
                .codeSunat(document.getCodeSunat())
                .name(document.getName())
                .description(document.getDescription())
                .build();
    }

    public Document toDocument(DocumentDTO documentDTO) {
        if (documentDTO == null) {return null;}

        return Document.builder()
                .id(documentDTO.getId())
                .code(documentDTO.getCode())
                .codeSunat(documentDTO.getCodeSunat())
                .name(documentDTO.getName())
                .description(documentDTO.getDescription())
                .build();
    }

    public CheckinDocumentDTO toCheckinDocumentDTO(Document document) {
        if (document == null) { return null; }

        return CheckinDocumentDTO.builder()
                .id(document.getId())                    // Long → Long ✓
                .documentType(document.getCode())        // String → String ✓
                .documentNumber(document.getCodeSunat()) // String → String ✓
                .issuer(document.getName())              // String → String ✓
                .issueDate(null)                         // LocalDate ✓
                .build();
    }

    public DocumentSummaryDTO toDocumentSummaryDTO(Document document) {
        if (document == null) { return null; }

        return DocumentSummaryDTO.builder()
                .id(document.getId())                    // Long → Long ✓
                .documentType(document.getCode())        // String → String ✓
                .reference(document.getCodeSunat())      // String → String ✓
                .build();
    }
}
