package com.project.ms_transaction.model.dto;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

    private int statusCode;
    private String message;
    private String details; // Para mostrar detalles como el ID del ProductWarehouse
    private Instant timestamp;

    // Constructores (vacío y con args)
    public ErrorResponseDTO (int statusCode, String message, String details) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
        this.timestamp = Instant.now();
    }

}
