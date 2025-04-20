package com.example.vacationcalculator.client;

import java.time.LocalDate;
import java.util.Set;

/**
 * Клиент для получения списка праздничных дней
 * <p>
 * Определяет метод для получения множества дат государственных праздников
 * для указанного календарного года
 * </p>
 */
public interface HolidayClient {
    Set<LocalDate> getHolidays(int year);
}