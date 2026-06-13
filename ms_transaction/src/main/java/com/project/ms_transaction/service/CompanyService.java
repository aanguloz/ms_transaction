package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.CompanyReqDTO;
import com.project.ms_transaction.model.dto.response.CompanyRespDTO;

import java.util.List;

public interface CompanyService {

    CompanyRespDTO addCompany(CompanyReqDTO companyReqDTO);

    List<CompanyRespDTO> getAllCompanies(String filter);

}
