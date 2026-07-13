package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.record.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.ClientReqDTO;
import com.project.ms_transaction.model.dto.response.ClientRespDTO;
import com.project.ms_transaction.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    public final ClientService clientService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addClient (
            @RequestBody ClientReqDTO clientReqDTO
    ) {
        ClientRespDTO result = clientService.addClient(clientReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, result, null));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> searchClient (
            @RequestParam("filter") String filter
    ) {
        List<ClientRespDTO> result = clientService.getAllClients(filter);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(true, result, null));
    }

}

