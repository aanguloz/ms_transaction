package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.ClientReqDTO;
import com.project.ms_transaction.model.dto.response.ClientRespDTO;
import com.project.ms_transaction.model.entity.Client;
import com.project.ms_transaction.model.entity.Company;
import com.project.ms_transaction.model.entity.IdentifierDocument;
import com.project.ms_transaction.model.entity.Person;
import com.project.ms_transaction.repository.ClientRepository;
import com.project.ms_transaction.repository.CompanyRepository;
import com.project.ms_transaction.repository.PersonRepository;
import com.project.ms_transaction.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;

    ClientRespDTO clientRespDTO;

    @Override
    public ClientRespDTO addClient(ClientReqDTO clientReqDTO) {
        if (clientReqDTO.getPerson() != null)  {
            if (personRepository.existsByNumberDocument(clientReqDTO.getPerson().getNumberDocument())) {
                Person person = personRepository.findByNumberDocument(clientReqDTO.getPerson().getNumberDocument());
                Client clientToSave = new Client(person, true);
                Client savedClient = clientRepository.save(clientToSave);
                return new ClientRespDTO(
                        savedClient.getPerson().getCompleteName(),
                        savedClient.getPerson().getNumberDocument(),
                        savedClient.getPerson().getAddress(),
                        savedClient.getPerson().getEmail(),
                        savedClient.getPerson().getPhoneNumber()
                );
            } else {
                Person person = new Person(
                        clientReqDTO.getPerson().getCompleteName(),
                        new IdentifierDocument(
                                clientReqDTO.getPerson().getTypeDocument().getId(),
                                clientReqDTO.getPerson().getTypeDocument().getCode(),
                                clientReqDTO.getPerson().getTypeDocument().getName(),
                                clientReqDTO.getPerson().getTypeDocument().getDescription(),
                                clientReqDTO.getPerson().getTypeDocument().getType()
                            ),
                        clientReqDTO.getPerson().getNumberDocument(),
                        clientReqDTO.getPerson().getPhoneNumber(),
                        clientReqDTO.getPerson().getAddress(),
                        clientReqDTO.getPerson().getEmail()
                );
                Person savedPerson = personRepository.save(person);
                Client clientToSave = new Client(savedPerson, true);
                Client savedClient = clientRepository.save(clientToSave);
                clientRespDTO = new ClientRespDTO(
                        savedClient.getPerson().getCompleteName(),
                        savedClient.getPerson().getNumberDocument(),
                        savedClient.getPerson().getAddress(),
                        savedClient.getPerson().getEmail(),
                        savedClient.getPerson().getPhoneNumber()
                );
            }
        } else if (clientReqDTO.getCompany() != null) {
            if (companyRepository.existsByRucNumber(clientReqDTO.getCompany().getRucNumber())) {
                Company company = companyRepository.findByRucNumber(clientReqDTO.getCompany().getRucNumber());
                Client clientToSave = new Client(company, true);
                Client savedClient = clientRepository.save(clientToSave);
                clientRespDTO = new ClientRespDTO(
                        savedClient.getCompany().getCompanyName(),
                        savedClient.getCompany().getRucNumber(),
                        savedClient.getCompany().getAddress(),
                        savedClient.getCompany().getEmail(),
                        savedClient.getCompany().getPhoneNumber()
                );
            } else {
                Company company = new Company(
                        clientReqDTO.getCompany().getCompanyName(),
                        clientReqDTO.getCompany().getRucNumber(),
                        clientReqDTO.getCompany().getAddress(),
                        clientReqDTO.getCompany().getEmail(),
                        clientReqDTO.getCompany().getPhoneNumber()
                );
                Company savedCompany = companyRepository.save(company);
                Client clientToSave = new Client(savedCompany, true);
                Client savedClient = clientRepository.save(clientToSave);
                clientRespDTO = new ClientRespDTO(
                        savedClient.getCompany().getCompanyName(),
                        savedClient.getCompany().getRucNumber(),
                        savedClient.getCompany().getAddress(),
                        savedClient.getCompany().getEmail(),
                        savedClient.getCompany().getPhoneNumber()
                );
            }
        }
        return clientRespDTO;
    }

    @Override
    public ClientRespDTO modifyClient(Long clientId, ClientReqDTO clientReqDTO) {
        return null;
    }

    @Override
    public List<ClientRespDTO> getAllClients(String filter) {
        return clientRepository.searchClient(filter);
    }
}
