package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.CheckOutReqDTO;
import com.project.ms_transaction.service.CheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/check-out")
@RequiredArgsConstructor
public class CheckOutController {

    private final CheckOutService checkOutService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addCheckOut(
            @RequestBody CheckOutReqDTO checkOutReqDTO
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, checkOutService.addCheckOut(checkOutReqDTO), null));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getCheckOut(
            @RequestParam String filter
    ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(true, checkOutService.getCheckOutList(filter), null));
    }

}
