package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.dto.response.ProductRespDTO;
import com.project.ms_transaction.model.entity.Product;

import java.util.List;

public interface ProductService {

    Product registerProduct(ProductReqDTO productReqDTO);

    Product updateProduct(ProductReqDTO productReqDTO);

    List<ProductRespDTO> listProductByWarehouse(String filter);

}
