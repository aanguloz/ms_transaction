package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.IdentifierDocumentDTO;
import com.project.ms_transaction.model.dto.request.IdentifierDocumentReqDTO;
import com.project.ms_transaction.model.entity.IdentifierDocument;
import com.project.ms_transaction.repository.IdentifierRepository;
import com.project.ms_transaction.service.IdentifierDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdentifierDocumentServiceImpl implements IdentifierDocumentService {

    private final IdentifierRepository identifierRepository;

    @Override
    public IdentifierDocumentDTO addIdentifierDocument(IdentifierDocumentReqDTO identifierDocumentReqDTO) {
        if (!identifierRepository.existsByName(identifierDocumentReqDTO.getName())) {
            IdentifierDocument identifierToSaved = new IdentifierDocument(identifierDocumentReqDTO.getCode(),identifierDocumentReqDTO.getName(), identifierDocumentReqDTO.getDescription(), identifierDocumentReqDTO.getType());
            IdentifierDocument savedIdentifier = identifierRepository.save(identifierToSaved);

            return new IdentifierDocumentDTO(
                    savedIdentifier.getId(),
                    savedIdentifier.getCode(),
                    savedIdentifier.getName(),
                    savedIdentifier.getDescription(),
                    savedIdentifier.getType()
            );
        } else
            throw new RuntimeException("Identifier with name " + identifierDocumentReqDTO.getName() + " already exists");
    }

    @Override
    public List<IdentifierDocumentDTO> getAllIdentifierDocuments(String filter) {
        return List.of();
    }
}
