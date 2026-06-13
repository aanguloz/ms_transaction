package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.DocumentDTO;

import java.util.List;

public interface DocumentService {

    void addDocument(DocumentDTO documentDTO);

    void updateDocument(DocumentDTO documentDTO);

    void deleteDocument(DocumentDTO documentDTO);

    List<DocumentDTO> getDocuments(String filter);

}
