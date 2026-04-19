package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.ClientReqDTO;
import com.project.ms_transaction.model.dto.response.ClientRespDTO;
import com.project.ms_transaction.model.entity.Client;
import com.project.ms_transaction.repository.ClientRepository;
import com.project.ms_transaction.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public void addClient(ClientReqDTO clientReqDTO) {
//        Client client = clientRepository.findByIdentifier(clientReqDTO.getIdentifier()).get();
    }

    @Override
    public void modifyClient(ClientReqDTO clientReqDTO) {
    }

    @Override
    public List<ClientRespDTO> getAllClients(String filter) {
//        return clientRepository.searchClient(filter).stream()
//                .map(row -> {
//                    // 1. Condicionamiento entre persona natural y razón social
//                    String name = row[0] != null ? (String) row[0] : (String) row[1];
//
//                    // 2. Mapeo seguro alineado con tu @AllArgsConstructor
//                    String identifier = (String) row[2];
//                    String address    = (String) row[3];
//                    String email      = (String) row[4];
//                    String phone      = (String) row[5]; // Corregido: antes no se estaba usando el índice correcto
//
//                    return new ClientRespDTO(name, identifier, address, email, phone);
//                })
//                .toList();

        return null;
    }
}
