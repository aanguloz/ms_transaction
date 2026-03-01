package com.project.ms_transaction.model.dto.request;

import com.project.ms_transaction.model.dto.CheckinProductEntryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckInReqDTO {

    String code;
    String numberDocument;
    String description;

    Long documentId;

    List<CheckinProductEntryDTO> products;

}
