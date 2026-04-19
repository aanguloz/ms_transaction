package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.CheckInReqDTO;
import com.project.ms_transaction.model.dto.response.CheckInRespDTO;
import com.project.ms_transaction.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/check-in")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addCheckIn(
            @RequestBody CheckInReqDTO checkInReqDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, checkInService.addCheckIn(checkInReqDTO), null));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getListCheckIn(
            @RequestParam(required = false) String filter
    ) {
        List<CheckInRespDTO> result = checkInService.getCheckinList(filter);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(true, result, null));
    }

}
