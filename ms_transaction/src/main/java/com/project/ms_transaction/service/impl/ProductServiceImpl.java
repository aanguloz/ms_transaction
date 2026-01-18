package com.project.ms_transaction.service.impl;

import com.project.ms_transaction.model.dto.request.ProductReqDTO;
import com.project.ms_transaction.model.entity.Checkin;
import com.project.ms_transaction.repository.CheckInRepository;
import com.project.ms_transaction.repository.CheckoutRepository;
import com.project.ms_transaction.repository.ProductRepository;
import com.project.ms_transaction.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CheckInRepository checkInRepository;
    private final CheckoutRepository checkoutRepository;

    @Override
    public void registerProduct(ProductReqDTO productReqDTO) {
        Checkin checkin = checkInRepository.findByCode(productReqDTO.getDocumentRef());
    }

    @Override
    public void updateProduct(ProductReqDTO productReqDTO) {

    }
}
