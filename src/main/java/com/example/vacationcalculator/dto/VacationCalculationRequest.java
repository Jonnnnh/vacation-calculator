package com.example.vacationcalculator.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * dto для передачи параметров расчёта отпускных через http‑запрос
 * <p>
 * Поля валидируются через Bean Validation:
 * <ul>
 *   <li>{@code averageMonthlySalary} — обязательное, положительное число</li>
 *   <li>{@code daysCount} — если указан, минимум 1</li>
 *   <li>{@code vacationDates} — если указан, минимум одна дата, каждая не null</li>
 * </ul>
 * Бизнес‑правило: обязательно должен быть указан **либо** {@code daysCount}, **либо**
 * непустой список {@code vacationDates} (проверяется методом {@link #isEitherDaysCountOrDates()})
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationCalculationRequest {

    @NotNull(message = "averageMonthlySalary обязательно")
    @Positive(message = "averageMonthlySalary должен быть > 0")
    private Double averageMonthlySalary;

    @Min(value = 1, message = "daysCount, если указан, — минимум 1")
    private Integer daysCount;

    @Size(min = 1, message = "vacationDates, если указан, — минимум 1 дата")
    private List<@NotNull(message = "date не может быть null") LocalDate> vacationDates;

    @AssertTrue(message = "Укажите либо daysCount, либо vacationDates, но не оба сразу")
    public boolean isEitherDaysCountOrDates() {
        boolean hasCount = daysCount != null;
        boolean hasDates = vacationDates != null && !vacationDates.isEmpty();
        return hasCount ^ hasDates;
    }
}