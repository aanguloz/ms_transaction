package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;
import com.project.ms_transaction.model.entity.Product;
import com.project.ms_transaction.repository.ProductRepository;
import com.project.ms_transaction.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void deleteProduct(Long productId) {

        Optional<Product> productDB = productRepository.findById(productId);
        if (productDB.isPresent())
        {
            Product product = (Product) productDB.get();
            product.setIsDeleted(true);
            productRepository.save(product);
        }
    }

    @Override
    public List<ProductRespDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(dto -> new ProductRespDTO(
                        dto.getId(),
                        dto.getName(),
                        dto.getDescription()
                        )
                ).collect(Collectors.toList());
    }
}
