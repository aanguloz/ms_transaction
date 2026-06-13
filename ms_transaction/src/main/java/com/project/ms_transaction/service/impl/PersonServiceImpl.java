package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.IdentifierDocumentDTO;
import com.project.ms_transaction.model.dto.request.PersonReqDTO;
import com.project.ms_transaction.model.dto.response.PersonRespDTO;
import com.project.ms_transaction.model.entity.IdentifierDocument;
import com.project.ms_transaction.model.entity.Person;
import com.project.ms_transaction.repository.IdentifierRepository;
import com.project.ms_transaction.repository.PersonRepository;
import com.project.ms_transaction.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final IdentifierRepository identifierRepository;

    @Override
    public PersonRespDTO addPerson(PersonReqDTO personReqDTO) {
        Person savedPerson = new Person();
        if (personRepository.existsByCompleteName(personReqDTO.getCompleteName())) {
            throw new RuntimeException("Person already exists");
        } else if (personRepository.existsByNumberDocument(personReqDTO.getNumberIdentifier())) {
            throw new RuntimeException("Person already exists");
        } else {
            Person personToSave = new Person(
                    personReqDTO.getCompleteName(),
                    prepareDocumentFromPerson(personReqDTO.getIdentifier()),
                    personReqDTO.getNumberIdentifier(),
                    personReqDTO.getPhoneNumber(),
                    personReqDTO.getAddress(),
                    personReqDTO.getEmail()
            );

            savedPerson = personRepository.save(personToSave);
        }

        return new PersonRespDTO(
                savedPerson.getId(),
                savedPerson.getCompleteName(),
                new IdentifierDocumentDTO(
                        savedPerson.getIdentifierDocument().getId(),
                        savedPerson.getIdentifierDocument().getCode(),
                        savedPerson.getIdentifierDocument().getName(),
                        savedPerson.getIdentifierDocument().getDescription(),
                        savedPerson.getIdentifierDocument().getType()
                ),
                savedPerson.getNumberDocument(),
                savedPerson.getPhoneNumber(),
                savedPerson.getAddress(),
                savedPerson.getEmail()
        );
    }

    IdentifierDocument prepareDocumentFromPerson(String name){
        return identifierRepository.findByName(name);
    }

    @Override
    public List<PersonRespDTO> getAllPersons(String filter) {
        return personRepository.findAllPerson(filter).stream().map(person -> new PersonRespDTO(
                (UUID) person[0],
                (String) person[1],
                mapToIdentifierDocumentDTO(person[2].toString()),
                (String) person[3],
                (String) person[4],
                (String) person[5],
                (String) person[6]
        )).toList();
    }

    IdentifierDocumentDTO mapToIdentifierDocumentDTO(String name) {
        if (name == null) return null;
        IdentifierDocument identifierDocument = identifierRepository.findByName(name);
        return new IdentifierDocumentDTO(
                identifierDocument.getId(),
                identifierDocument.getCode(),
                identifierDocument.getName(),
                identifierDocument.getDescription(),
                identifierDocument.getType()
        );
    }


}
