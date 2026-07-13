package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.record.ApiResponseDTO;
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
    ) {
        List<ProductWarehouseRespDTO> result = productWarehouseService.listProductByWarehouse(filter);
        return ResponseEntity.status(200).body(new ApiResponseDTO<>(true, result, null));
    }

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addProductWarehouse(
            @RequestBody ProductWarehouseReqDTO productWarehouseReqDTO
    ) {
        productWarehouseService.addProductWarehouse(productWarehouseReqDTO);
        return ResponseEntity.status(201).body(new ApiResponseDTO<>(true, "ProductWarehouse was created.", null));
    }

    @PutMapping("/")
    ResponseEntity<ApiResponseDTO<?>> modifyProductWarehouse(
            @RequestBody ProductWarehouseReqDTO productWarehouseReqDTO
    ) {
        productWarehouseService.modifyProductWarehouse(productWarehouseReqDTO);
        return ResponseEntity.status(200).body(new ApiResponseDTO<>(true, "ProductWarehouse was updated.", null));

    }

}
