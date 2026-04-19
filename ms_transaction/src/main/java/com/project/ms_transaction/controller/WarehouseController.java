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
            List<WarehouseRespDTO>result = warehouseService.warehouseList(filter);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, result, null));
    }

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addWarehouse(
            @RequestBody WareHouseReqDTO warehouseRespDTO
    ) {
            warehouseService.createWareHouse(warehouseRespDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Warehouse Register", null));
    }

}
