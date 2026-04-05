package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.CheckInReqDTO;
import com.project.ms_transaction.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/check-in")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addCheckIn(
            @RequestBody CheckInReqDTO checkInReqDTO
    ){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, checkInService.addCheckIn(checkInReqDTO), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
//
//    @GetMapping("/")
//    ResponseEntity<ApiResponseDTO<?>> getListCheckIn(
//            @RequestParam(required = false) String filter
//    ){
//        try{
//            List<CheckinListItem> result = checkInService.getCheckinList(filter);
//            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, result, null));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
//        }
//    }

}
