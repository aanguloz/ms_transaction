package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.PersonReqDTO;
import com.project.ms_transaction.model.dto.response.PersonRespDTO;

import java.util.List;

public interface PersonService {

    PersonRespDTO addPerson(PersonReqDTO personReqDTO);

    List<PersonRespDTO> getAllPersons(String filter);

}
