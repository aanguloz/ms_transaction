package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.CompanyReqDTO;
import com.project.ms_transaction.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addCompany(
            @RequestBody CompanyReqDTO companyReqDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, companyService.addCompany(companyReqDTO), null));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getCompany(
            @RequestParam String filter
    ) {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, companyService.getAllCompanies(filter), null));
    }
}
