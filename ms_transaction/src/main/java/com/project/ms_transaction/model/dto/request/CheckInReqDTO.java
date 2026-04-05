package com.project.ms_transaction.model.dto.request;

import com.project.ms_transaction.model.dto.CheckInItemDTO;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckInReqDTO {

    private String code;
    private String numberDocument;
    private String description;

    private Long documentId;

    private List<CheckInItemDTO> items;

}
