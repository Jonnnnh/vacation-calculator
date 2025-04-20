package com.example.vacationcalculator.service.impl;

import com.example.vacationcalculator.client.HolidayClient;
import com.example.vacationcalculator.service.BusinessDaysCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link BusinessDaysCalculatorImpl} — реализация подсчёта рабочих дней
 * <p>
 * Считает количество рабочих дней в заданном списке дат, исключая:
 * <ul>
 *   <li>выходные дни (суббота, воскресенье)</li>
 *   <li>праздничные дни, полученные через {@link HolidayClient}</li>
 * </ul>
 * Логи уровня debug и trace помогают отследить ход расчёта:
 * <ul>
 *   <li>debug — старт/финиш расчёта, извлечение годов и праздников</li>
 *   <li>trace — почему та или иная дата не засчитана в рабочие</li>
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessDaysCalculatorImpl implements BusinessDaysCalculator {
    private final HolidayClient holidayClient;

    @Override
    public int calculateBusinessDays(List<LocalDate> dates) {
        if (dates == null || dates.isEmpty()) {
            throw new IllegalArgumentException("Список дат для расчёта не может быть пустым");
        }

        log.debug("Начало расчёта рабочих дней для дат: {}", dates);
        Set<Integer> years = dates.stream()
                .map(LocalDate::getYear)
                .collect(Collectors.toSet());
        log.debug("Уникальные года для получения праздников: {}", years);

        Set<LocalDate> holidays = years.stream()
                .flatMap(year -> {
                    log.debug("Запрос праздников для года {}", year);
                    return holidayClient.getHolidays(year).stream();
                })
                .collect(Collectors.toSet());
        log.debug("Всего получено праздников: {} → {}", holidays.size(), holidays);

        int businessDays = (int) dates.stream()
                .filter(date -> {
                    DayOfWeek wd = date.getDayOfWeek();
                    boolean isWeekend = wd == DayOfWeek.SATURDAY || wd == DayOfWeek.SUNDAY;
                    boolean isHoliday = holidays.contains(date);

                    if (isWeekend) {
                        log.trace("Дата {} — выходной", date);
                    } else if (isHoliday) {
                        log.trace("Дата {} — праздничный день", date);
                    } else {
                        log.trace("Дата {} засчитана как рабочий день", date);
                    }

                    return !isWeekend && !isHoliday;
                })
                .count();

        log.debug("Результат: {} рабочих дней из {} проверенных дат", businessDays, dates.size());
        return businessDays;
    }
}