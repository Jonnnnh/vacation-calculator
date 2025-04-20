package com.example.vacationcalculator.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация, отвечающая за подключение свойств Holiday API
 * <p>
 * Включает бин {@link HolidayApiProperties}, связывающий
 * внешние параметры по префиксу {@code vacation-calculator.holiday-api}
 * </p>
 */
@Configuration
@EnableConfigurationProperties(HolidayApiProperties.class)
public class HolidayConfig {
}