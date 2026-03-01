package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;

import java.util.List;

public interface ProductService {

    ProductRespDTO createProduct(ProductReqDTO productReqDTO);

    ProductRespDTO updateProduct(Long idProduct, ProductReqDTO productReqDTO);

    void deleteProduct(Long productId);

    List<ProductRespDTO> getAllProducts();

}
