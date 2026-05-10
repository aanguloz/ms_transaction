package com.project.ms_transaction.model.mapper;

import com.project.ms_transaction.model.dto.CheckoutProductDTO;
import com.project.ms_transaction.model.dto.request.CheckOutReqDTO;
import com.project.ms_transaction.model.dto.response.CheckOutRespDTO;
import com.project.ms_transaction.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CheckOutMapper {

    private final DocumentMapper documentMapper;

    public Checkout toEntity(CheckOutReqDTO dto) {
        if (dto == null) return null;

        Checkout checkout = new Checkout();
        checkout.setCode(dto.getCode());
        checkout.setNumberDocument(dto.getNumberDocument());
        checkout.setDescription(dto.getDescription());

        if(dto.getDocumentId() != null){
            Document docRef = new Document();
            docRef.setId(dto.getDocumentId());
            checkout.setDocument(docRef);
        }

        checkout.setDetails(new ArrayList<>());
        checkout.setProductWarehouses(new HashSet<>());

        return checkout;
    }

    public CheckOutRespDTO toRespDTO(Checkout checkout){
        if (checkout == null) return null;

        return CheckOutRespDTO.builder()
                .id(checkout.getId())
                .code(checkout.getCode())
                .numberDocument(checkout.getNumberDocument())
                .description(checkout.getDescription())
                .creationDate(checkout.getCreatedAt())
                .document(documentMapper.toCheckoutDocumentDTO(checkout.getDocument()))
                .products(mapProducts(checkout.getDetails()))
                .totalProducts(checkout.getDetails() != null ? checkout.getDetails().size() : 0)
                .totalValue(calculateTotalValue(checkout.getDetails()))
                .build();
    }

    private List<CheckoutProductDTO> mapProducts(List<CheckoutDetail> details) {
        if (details == null) return Collections.emptyList();

        return details.stream().map(detail -> {
            ProductWarehouse pw = detail.getProductWarehouse();
            if (pw == null) return null;

            // Obtener primer warehouse asociado (para mostrar en lista)
            String warehouseName = pw.getWarehouses() != null && !pw.getWarehouses().isEmpty()
                    ? pw.getWarehouses().iterator().next().getName()
                    : "Sin asignar";

            return CheckoutProductDTO.builder()
                    .productWarehouseId(pw.getId())
                    .productName(pw.getName())
                    .warehouseName(warehouseName)
                    .quantity(detail.getQuantityOuted())
                    .unitCost(pw.getPrice() != null ? Double.parseDouble(pw.getPrice().toString()) : null)
                    .unitPrice(pw.getPrice() != null ? Double.parseDouble(pw.getPrice().toString()) : null)
                    .expirationDate(pw.getExpirationDate())
                    .observation(detail.getObservation())
                    .build();
        }).filter(dto -> dto != null).collect(Collectors.toList());
    }

    private BigDecimal calculateTotalValue(List<CheckoutDetail> details) {
        if (details == null) return BigDecimal.ZERO;

        return details.stream()
                .map(detail -> {
                    ProductWarehouse pw = detail.getProductWarehouse();
                    if (pw == null || pw.getPrice() == null || detail.getQuantityOuted() == null)
                        return BigDecimal.ZERO;
                    return BigDecimal.valueOf(pw.getPrice())
                            .multiply(BigDecimal.valueOf(detail.getQuantityOuted()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
