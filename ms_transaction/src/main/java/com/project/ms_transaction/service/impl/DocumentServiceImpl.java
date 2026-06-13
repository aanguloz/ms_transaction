package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.DocumentDTO;
import com.project.ms_transaction.model.entity.Document;
import com.project.ms_transaction.model.enums.DocumentType;
import com.project.ms_transaction.repository.DocumentRepository;
import com.project.ms_transaction.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public void addDocument(DocumentDTO documentDTO) {
        if(!documentRepository.existsByName(documentDTO.getName())) {
            Document documentToSave = new Document(documentDTO.getCode(), documentDTO.getCodeSunat(), documentDTO.getName(), documentDTO.getDescription(), documentDTO.getDocumentType());
            documentRepository.save(documentToSave);
        } else {
            throw new RuntimeException("Document with name " + documentDTO.getName() + " already exists.");
        }
    }

    @Override
    public void updateDocument(DocumentDTO documentDTO) {

    }

    @Override
    public void deleteDocument(DocumentDTO documentDTO) {

    }

    @Override
    public List<DocumentDTO> getDocuments(String filter) {
        List<Object[]> list = documentRepository.listDocument(filter);
        return list.stream().map(obj -> new DocumentDTO(
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                obj[5] != null ? DocumentType.valueOf((String) obj[5]) : null
        )).toList();
    }
}
