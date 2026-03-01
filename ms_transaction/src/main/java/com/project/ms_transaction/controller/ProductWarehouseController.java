package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.ProductWarehouseReqDTO;
import com.project.ms_transaction.model.dto.response.ProductWarehouseRespDTO;
import com.project.ms_transaction.service.ProductWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-warehouse")
@RequiredArgsConstructor
public class ProductWarehouseController {

    private final ProductWarehouseService productWarehouseService;

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getProductByWarehouse(
            @RequestParam(required = false) String filter
    ){
        try {
            List<ProductWarehouseRespDTO> result = productWarehouseService.listProductByWarehouse(filter);
            return ResponseEntity.status(200).body(new ApiResponseDTO<>(true, result, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addProduct(
            @RequestBody ProductWarehouseReqDTO productWarehouseReqDTO
    ){
        try{
            productWarehouseService.registerProduct(productWarehouseReqDTO);
            return ResponseEntity.status(201).body(new ApiResponseDTO<>(true, "ProductWarehouse was created.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/")
    ResponseEntity<ApiResponseDTO<?>> updateProduct(
            @RequestBody ProductWarehouseReqDTO productWarehouseReqDTO
    ){
        try{
            productWarehouseService.modifyProduct(productWarehouseReqDTO);
            return ResponseEntity.status(200).body(new ApiResponseDTO<>(true, "ProductWarehouse was updated.", null));

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

}
