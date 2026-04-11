package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getInventory() {
        try {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, inventoryService.inventoryList(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, null, e.getMessage()));
        }
    }

}
