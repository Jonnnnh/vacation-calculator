package com.example.vacationcalculator.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, сигнализирующее о нарушении валидации входных данных
 * <p>
 * Бросается в сервисном слое при обнаружении некорректных параметров,
 * которые не проходят проверки бизнес‑валидации.
 * Отвечает http‑статусом 400 bad request с кодом «VALIDATION_FAILED»
 * </p>
 */
public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super("VALIDATION_FAILED", message, HttpStatus.BAD_REQUEST);
    }
}