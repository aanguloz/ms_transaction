package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.WareHouseReqDTO;
import com.project.ms_transaction.model.dto.response.WarehouseRespDTO;
import com.project.ms_transaction.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> searchWarehouse(
            @RequestParam(required = false) String filter
    ) {
        try {
            List<WarehouseRespDTO>result = warehouseService.warehouseList(filter);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, result, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, e.getCause(), e.getMessage()));
        }
    }

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addWarehouse(
            @RequestBody WareHouseReqDTO warehouseRespDTO
    ) {
        try {
            warehouseService.createWareHouse(warehouseRespDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Warehouse Register", null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, e.getCause(), e.getMessage()));
        }
    }

}
