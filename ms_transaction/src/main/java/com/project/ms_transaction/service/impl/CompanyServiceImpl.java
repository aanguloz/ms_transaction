package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.CompanyReqDTO;
import com.project.ms_transaction.model.dto.response.CompanyRespDTO;
import com.project.ms_transaction.model.entity.Company;
import com.project.ms_transaction.repository.CompanyRepository;
import com.project.ms_transaction.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyRespDTO addCompany(CompanyReqDTO companyReqDTO) {
        if (companyRepository.existsByCompanyName(companyReqDTO.getCompanyName())) {
            throw new RuntimeException("Company with name " + companyReqDTO.getCompanyName() + " already exists");
        } else if (companyRepository.existsByRucNumber(companyReqDTO.getRucNumber())) {
            throw new RuntimeException("Company with RUC number " + companyReqDTO.getRucNumber() + " already exists");
        } else {
            Company companyToSave = new Company(companyReqDTO.getCompanyName(), companyReqDTO.getRucNumber(), companyReqDTO.getAddress(), companyReqDTO.getEmail(), companyReqDTO.getPhoneNumber());
            Company savedCompany = companyRepository.save(companyToSave);

            return new CompanyRespDTO(
                    savedCompany.getId(),
                    savedCompany.getCompanyName(),
                    savedCompany.getRucNumber(),
                    savedCompany.getAddress(),
                    savedCompany.getEmail(),
                    savedCompany.getPhoneNumber()
            );
        }
    }

    @Override
    public List<CompanyRespDTO> getAllCompanies(String filter) {
        return companyRepository.getCompanies(filter).stream()
                .map(objects -> new CompanyRespDTO(
                        (UUID) objects[0],
                        (String) objects[1],
                        (String) objects[2],
                        (String) objects[3],
                        (String) objects[4],
                        (String) objects[5]
                ))
                .toList();
    }
}
