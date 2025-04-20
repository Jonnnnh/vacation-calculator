package com.example.vacationcalculator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Свойства для расчёта отпускных выплат
 * <p>
 * Связываются с конфигурацией по префиксу {@code vacation-calculator.pay}
 * Содержат параметр {@code dayDivisor} — делитель для расчёта средней дневной зарплаты
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "vacation-calculator.pay")
public class VacationPayProperties {

    /**
     * Делитель для вычисления среднего числа дней в месяце (используется вместо 30.0)
     * По умолчанию 29.3, согласно Постановлению Правительства РФ №642 от 10.07.2014 (ред. от 01.04.2022)
     */
    private double dayDivisor = 29.3;
}