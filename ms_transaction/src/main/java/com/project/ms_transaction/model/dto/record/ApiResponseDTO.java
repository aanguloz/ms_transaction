package com.project.ms_transaction.model.dto.record;

public record ApiResponseDTO<T>(
        boolean succes,
        T data,
        String message
) {}
