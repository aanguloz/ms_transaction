package com.project.ms_transaction.model.dto.response;

import com.project.ms_transaction.model.dto.CheckoutDocumentDTO;
import com.project.ms_transaction.model.dto.CheckoutProductDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRespDTO {

    private Long id;
    private String code;
    private String numberDocument;
    private String description;
    private Long clientId;
    private Instant creationDate;

    private CheckoutDocumentDTO document;

    private List<CheckoutProductDTO> products;

    private Integer totalProducts;
    private Double totalValue;

}
