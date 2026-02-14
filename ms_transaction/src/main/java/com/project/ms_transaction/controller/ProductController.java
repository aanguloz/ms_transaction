package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;
import com.project.ms_transaction.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getProductByWarehouse(
            @RequestParam(required = false) String filter
    ){
        try {
            List<ProductRespDTO> result = productService.listProductByWarehouse(filter);
            return ResponseEntity.status(200).body(new ApiResponseDTO<>(true, result, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addProduct(
            @RequestBody ProductReqDTO productReqDTO
    ){
        try{
            productService.registerProduct(productReqDTO);
            return ResponseEntity.status(201).body(new ApiResponseDTO<>(true, "Product was created.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

}
