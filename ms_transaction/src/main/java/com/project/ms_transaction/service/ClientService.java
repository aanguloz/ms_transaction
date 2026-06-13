package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ClientReqDTO;
import com.project.ms_transaction.model.dto.response.ClientRespDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ClientService {

    ClientRespDTO addClient(@RequestBody ClientReqDTO clientReqDTO);

    ClientRespDTO modifyClient(Long clientId, @RequestBody ClientReqDTO clientReqDTO);

    List<ClientRespDTO> getAllClients(String filter);

}
