package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.PersonReqDTO;
import com.project.ms_transaction.model.dto.response.PersonRespDTO;
import com.project.ms_transaction.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addPerson(
            @RequestBody PersonReqDTO personReqDTO
    ) {
        PersonRespDTO personRespDTO = personService.addPerson(personReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, personRespDTO, null));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getPerson(
            @RequestParam String filter
    ) {
        List<PersonRespDTO> personRespDTOs = personService.getAllPersons(filter);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, personRespDTOs, null));
    }
}
