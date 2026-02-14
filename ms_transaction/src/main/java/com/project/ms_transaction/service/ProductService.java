package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;

public interface ProductService {

    ProductRespDTO createProduct(ProductReqDTO productReqDTO);

    ProductRespDTO updateProduct(ProductReqDTO productReqDTO);

    ProductRespDTO deleteProduct(Long productId);

    ProductRespDTO getAllProducts(ProductReqDTO productReqDTO);

}
