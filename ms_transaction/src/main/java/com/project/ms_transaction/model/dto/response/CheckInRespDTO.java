package com.project.ms_transaction.model.dto.response;

import com.project.ms_transaction.model.dto.CheckinProductDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class CheckInRespDTO {

    private Long id;
    private String code;
    private String numberDocument;
    private String description;
    private Instant creationDate;

    // Documento completo (solo si es necesario en el contexto)
    private CheckinDocumentDTO document;

    // Lista de productos con sus detalles
    private List<CheckinProductDTO> products;

    // Metadata útil para el frontend
    private Integer totalProducts;
    private BigDecimal totalValue; // Calculado a partir de los productos

}
