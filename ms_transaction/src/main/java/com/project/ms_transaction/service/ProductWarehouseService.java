package com.project.ms_transaction.service;

import com.project.ms_transaction.model.dto.request.ProductWarehouseReqDTO;
import com.project.ms_transaction.model.dto.response.ProductWarehouseRespDTO;
import com.project.ms_transaction.model.entity.ProductWarehouse;

import java.util.List;

public interface ProductWarehouseService {

    ProductWarehouse addProductWarehouse(ProductWarehouseReqDTO productWarehouseReqDTO);

    ProductWarehouse modifyProductWarehouse(ProductWarehouseReqDTO productWarehouseReqDTO);

    List<ProductWarehouseRespDTO> listProductByWarehouse(String filter);

}
