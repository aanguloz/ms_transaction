package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ProductWarehouseReqDTO;
import com.project.ms_transaction.model.dto.response.ProductWarehouseRespDTO;
import com.project.ms_transaction.model.entity.ProductWarehouse;

import java.util.List;

public interface ProductWarehouseService {

    ProductWarehouse registerProduct(ProductWarehouseReqDTO productWarehouseReqDTO);

    ProductWarehouse updateProduct(ProductWarehouseReqDTO productWarehouseReqDTO);

    List<ProductWarehouseRespDTO> listProductByWarehouse(String filter);

}
