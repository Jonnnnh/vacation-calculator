package com.example.vacationcalculator.service.impl;

import com.example.vacationcalculator.config.VacationPayProperties;
import com.example.vacationcalculator.exception.BusinessErrorException;
import com.example.vacationcalculator.service.VacationPayCalculator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Стандартная реализация расчёта отпускных выплат
 * <p>
 * При расчёте используется нормативное среднее количество календарных дней в месяце — 29.3,
 * в соответствии с Постановлением Правительства РФ №642 от 10.07.2014 (в ред. от 01.04.2022)
 * </p>
 */
@Slf4j
@Service
@AllArgsConstructor
public class VacationPayCalculatorImpl implements VacationPayCalculator {

    private final VacationPayProperties props;

    @Override
    public double calculateVacationPay(double avgMonthlySalary, int workingDays) {
        log.debug("Расчёт отпускных: средняя зарплата = {}, рабочих дней = {}",
                avgMonthlySalary, workingDays);
        if (avgMonthlySalary <= 0) {
            throw new BusinessErrorException("INVALID_SALARY", "Зарплата должна быть положительной");
        }
        if (workingDays <= 0) {
            throw new BusinessErrorException("INVALID_DAYS", "Количество рабочих дней должно быть положительным");
        }
        double divisor = props.getDayDivisor();
        double dailyRate = avgMonthlySalary / divisor;
        double result = Math.round(dailyRate * workingDays * 100) / 100.0;

        log.debug("Итого отпускных = {}", result);
        return result;
    }
}