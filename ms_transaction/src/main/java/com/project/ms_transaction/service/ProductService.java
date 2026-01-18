package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;

public interface ProductService {

    void registerProduct(ProductReqDTO productReqDTO);

    void updateProduct(ProductReqDTO productReqDTO);

}
