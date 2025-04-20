package com.example.vacationcalculator.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

/**
 * dto для представления структурированной информации об ошибке api
 * <p>
 * используется в {@code GlobalExceptionHandler} для формирования ответа
 * клиенту при возникновении ошибок валидации или бизнес‑логики
 * </p>
 */
@Data
@Builder
public class ApiErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private Map<String, String> details;
}