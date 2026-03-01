package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.ApiResponseDTO;
import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;
import com.project.ms_transaction.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addProduct(
            @RequestBody ProductReqDTO productReqDTO
    )
    {
        try {
            ProductRespDTO result = productService.createProduct(productReqDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, result, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/")
    ResponseEntity<ApiResponseDTO<?>> modifyProduct(
            @RequestParam Long idProduct,
            @RequestBody ProductReqDTO productReqDTO
    )
    {
        try {
            ProductRespDTO result = productService.updateProduct(idProduct, productReqDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(true, result, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getProduct()
    {
        try{
            List<ProductRespDTO> result = productService.getAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(true, result, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

}
