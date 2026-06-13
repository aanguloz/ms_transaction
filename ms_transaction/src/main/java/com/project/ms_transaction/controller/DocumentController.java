package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.DocumentDTO;
import com.project.ms_transaction.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addDocument(
            @RequestBody DocumentDTO documentDTO
    ) {
        documentService.addDocument(documentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, "Document has registere... Success", null));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getDocument(
            @RequestParam String filter
    ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(true, documentService.getDocuments(filter), null));
    }

}
