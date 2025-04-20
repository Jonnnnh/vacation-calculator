package com.example.vacationcalculator.service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для расчёта количества рабочих дней среди заданного списка дат
 * <p>
 * Рабочими днями считаются все дни, кроме субботы, воскресенья и праздничных дней,
 * которые поставляются через {@link com.example.vacationcalculator.client.HolidayClient}
 * </p>
 */
public interface BusinessDaysCalculator {
    int calculateBusinessDays(List<LocalDate> dates);
}