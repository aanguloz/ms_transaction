package com.project.ms_transaction.model.dto.request;

import com.project.ms_transaction.model.dto.CheckOutItemDTO;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckOutReqDTO {

    private String code;
    private String numberDocument;
    private String description;

    private Long documentId;

    private Long clientId;

    private List<CheckOutItemDTO> items;

}
