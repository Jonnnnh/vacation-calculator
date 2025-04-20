package com.example.vacationcalculator.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * базовый абстрактный класс для исключений в api
 * <p>
 * содержит информацию об http‑статусе, который следует вернуть клиенту,
 * и о машинно‑читаемом коде ошибки для программистов
 * </p>
 */
@Getter
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    protected ApiException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}