package com.example.vacationcalculator.exception;

import org.springframework.http.HttpStatus;

/**
 * исключение, обозначающее нарушение бизнес‑логики в процессе расчёта
 * <p>
 * используется для сигнализации о том, что входные данные
 * формально валидны, но противоречат бизнес‑правилам сервиса
 * возвращает http‑статус 422 (unprocessable entity)
 * </p>
 */
public class BusinessErrorException extends ApiException {

    public BusinessErrorException(String code, String message) {
        super(code, message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}