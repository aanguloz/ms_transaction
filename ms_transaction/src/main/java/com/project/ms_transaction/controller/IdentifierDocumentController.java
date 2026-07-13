package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.record.ApiResponseDTO;
import com.project.ms_transaction.model.dto.IdentifierDocumentDTO;
import com.project.ms_transaction.model.dto.request.IdentifierDocumentReqDTO;
import com.project.ms_transaction.service.IdentifierDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/identifier")
@RequiredArgsConstructor
public class IdentifierDocumentController {

    private final IdentifierDocumentService identifierDocumentService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addIdentifierDocument(
            @RequestBody IdentifierDocumentReqDTO identifierDocumentReqDTO
    ) {
        IdentifierDocumentDTO identifierDocumentDTO = identifierDocumentService.addIdentifierDocument(identifierDocumentReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, identifierDocumentDTO, null));
    }

}
