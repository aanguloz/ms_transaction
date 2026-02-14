package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;
import com.project.ms_transaction.model.entity.Product;
import com.project.ms_transaction.repository.ProductRepository;
import com.project.ms_transaction.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    @Override
    public ProductRespDTO createProduct(ProductReqDTO productReqDTO) {
        if (!productRepository.existsByName(productReqDTO.getName())) {
            Product productToSave = new Product(productReqDTO.getName(), productReqDTO.getDescription());
            Product savedProduct = productRepository.save(productToSave);

            return new ProductRespDTO(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription());
        } else {
            throw new RuntimeException("Product with name " + productReqDTO.getName() + " already exists.");
        }
    }

    @Override
    public ProductRespDTO updateProduct(ProductReqDTO productReqDTO) {
        Optional<Product> existingProductOpt = productRepository.findByName(productReqDTO.getName());
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setName(productReqDTO.getName());
            existingProduct.setDescription(productReqDTO.getDescription());
            Product updatedProduct = productRepository.save(existingProduct);

            return new ProductRespDTO(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getDescription());
        } else {
            throw new RuntimeException("Product with name " + productReqDTO.getName() + " does not exist.");
        }
    }

    @Override
    public ProductRespDTO deleteProduct(Long productId) {
        return null;
    }

    @Override
    public ProductRespDTO getAllProducts(ProductReqDTO productReqDTO) {
        return null;
    }
}
