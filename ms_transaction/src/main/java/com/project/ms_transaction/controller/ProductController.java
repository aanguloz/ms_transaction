package com.project.ms_transaction.controller;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;
import com.project.ms_transaction.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/")
    ResponseEntity<String> addProduct(
            @RequestBody ProductReqDTO productReqDTO
    )
    {
        try {
            ProductRespDTO productRespDTO = productService.createProduct(productReqDTO);
            return ResponseEntity.ok().body("success");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
