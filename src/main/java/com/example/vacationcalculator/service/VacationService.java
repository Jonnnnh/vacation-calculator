package com.example.vacationcalculator.service;

import com.example.vacationcalculator.dto.VacationCalculationRequest;
import com.example.vacationcalculator.dto.VacationCalculationResponse;

/**
 * Сервис для расчёта отпускных выплат
 * <p>
 * Инкапсулирует всю бизнес‑логику: от валидации входного дто до вычисления
 * итоговой суммы выплат
 * </p>
 */
public interface VacationService {
    VacationCalculationResponse calculate(VacationCalculationRequest request);
}