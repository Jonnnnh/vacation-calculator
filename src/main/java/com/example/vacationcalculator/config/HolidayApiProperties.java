package com.example.vacationcalculator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Свойства доступа к Holiday API
 * <p>
 * Связываются с конфигурацией по префиксу {@code vacation-calculator.holiday-api}
 * Содержат базовый url и api‑ключ для запросов
 * </p>
 *
 * @param url    базовый url Holiday API, например https://holidayapi.com/v1/holidays
 * @param apiKey ключ для авторизации при вызовах api
 */
@Validated
@ConfigurationProperties(prefix = "vacation-calculator.holiday-api", ignoreUnknownFields = false)
public record HolidayApiProperties(
        String url,
        String apiKey
) {}