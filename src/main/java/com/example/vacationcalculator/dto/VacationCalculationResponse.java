package com.example.vacationcalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * dto для передачи результата расчёта отпускных выплат
 * содержит итоговую сумму отпускных, рассчитанную сервисом
 */
@Data
@AllArgsConstructor
public class VacationCalculationResponse {
    private double totalAmount;
}