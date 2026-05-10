package com.project.ms_transaction.model.mapper;

import com.project.ms_transaction.model.dto.CheckinListItem;
import com.project.ms_transaction.model.dto.CheckinProductDTO;
import com.project.ms_transaction.model.dto.request.CheckInReqDTO;
import com.project.ms_transaction.model.dto.response.CheckInRespDTO;
import com.project.ms_transaction.model.entity.Checkin;
import com.project.ms_transaction.model.entity.CheckinDetail;
import com.project.ms_transaction.model.entity.Document;
import com.project.ms_transaction.model.entity.ProductWarehouse;
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
public class CheckInMapper {

    private final DocumentMapper documentMapper;

    public Checkin toEntity(CheckInReqDTO dto) {
        if (dto == null) return null;

        Checkin checkIn = new Checkin();
        checkIn.setCode(dto.getCode());
        checkIn.setNumberDocument(dto.getNumberDocument());
        checkIn.setDescription(dto.getDescription());

        if(dto.getDocumentId() != null) {
            Document docRef = new Document();
            docRef.setId(dto.getDocumentId());
            checkIn.setDocument(docRef);
        }

        checkIn.setDetails(new ArrayList<>());
        checkIn.setProductWarehouses(new HashSet<>());

        return checkIn;
    }

    public CheckInRespDTO toRespDTO(Checkin checkIn) {
        if (checkIn == null) return null;

        return CheckInRespDTO.builder()
                .id(checkIn.getId())
                .code(checkIn.getCode())
                .numberDocument(checkIn.getNumberDocument())
                .description(checkIn.getDescription())
                .creationDate(checkIn.getCreatedAt()) // Asume @CreationTimestamp en entidad
                .document(documentMapper.toCheckinDocumentDTO(checkIn.getDocument()))
                .products(mapProducts(checkIn.getDetails()))
                .totalProducts(checkIn.getDetails() != null ? checkIn.getDetails().size() : 0)
                .totalValue(calculateTotalValue(checkIn.getDetails()))
                .build();
    }

    public CheckinListItem toListItem(Checkin checkIn){
        if (checkIn == null) return null;

        return CheckinListItem.builder()
                .id(checkIn.getId())
                .code(checkIn.getCode())
                .numberDocument(checkIn.getNumberDocument())
                .description(checkIn.getDescription())
                .creationDate(checkIn.getCreatedAt())
                .document(documentMapper.toDocumentSummaryDTO(checkIn.getDocument()))
                .productsCount(checkIn.getDetails() != null ? checkIn.getDetails().size() : 0)
                .build();
    }

    private List<CheckinProductDTO> mapProducts(List<CheckinDetail> details) {
        if (details == null) return Collections.emptyList();

        return details.stream().map(detail -> {
            ProductWarehouse pw = detail.getProductWarehouse();
            if (pw == null) return null;

            // Obtener primer warehouse asociado (para mostrar en lista)
            String warehouseName = pw.getWarehouses() != null && !pw.getWarehouses().isEmpty()
                    ? pw.getWarehouses().iterator().next().getName()
                    : "Sin asignar";

            return CheckinProductDTO.builder()
                    .productWarehouseId(pw.getId())
                    .productName(pw.getName())
                    .warehouseName(warehouseName)
                    .quantity(detail.getQuantityReceived())
                    .unitCost(pw.getPrice() != null ? Double.parseDouble(pw.getPrice().toString()) : null)
                    .unitPrice(pw.getPrice() != null ? Double.parseDouble(pw.getPrice().toString()) : null)
                    .expirationDate(pw.getExpirationDate())
                    .observation(detail.getObservation())
                    .build();
        }).filter(dto -> dto != null).collect(Collectors.toList());
    }

    private BigDecimal calculateTotalValue(List<CheckinDetail> details) {
        if (details == null) return BigDecimal.ZERO;

        return details.stream()
                .map(detail -> {
                    ProductWarehouse pw = detail.getProductWarehouse();
                    if (pw == null || pw.getPrice() == null || detail.getQuantityReceived() == null)
                        return BigDecimal.ZERO;
                    return BigDecimal.valueOf(pw.getPrice())
                            .multiply(BigDecimal.valueOf(detail.getQuantityReceived()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
