package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.IdentifierDocumentDTO;
import com.project.ms_transaction.model.dto.request.IdentifierDocumentReqDTO;

import java.util.List;

public interface IdentifierDocumentService {

    IdentifierDocumentDTO addIdentifierDocument(IdentifierDocumentReqDTO identifierDocumentReqDTO);

    List<IdentifierDocumentDTO> getAllIdentifierDocuments(String filter);

}
