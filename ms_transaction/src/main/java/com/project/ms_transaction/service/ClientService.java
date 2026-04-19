package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ClientReqDTO;
import com.project.ms_transaction.model.dto.response.ClientRespDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ClientService {

    void addClient(@RequestBody ClientReqDTO clientReqDTO);

    void modifyClient(@RequestBody ClientReqDTO clientReqDTO);

    List<ClientRespDTO> getAllClients(String filter);

}
