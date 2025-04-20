package com.example.vacationcalculator.controller;

import com.example.vacationcalculator.dto.VacationCalculationRequest;
import com.example.vacationcalculator.dto.VacationCalculationResponse;
import com.example.vacationcalculator.service.VacationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * rest‑контроллер для расчёта отпускных выплат
 * <p>
 * Эндпоинт: <code>GET /calculate</code><br>
 * На вход принимает параметры, маппятся в {@link VacationCalculationRequest}:
 * <ul>
 *   <li><b>averageMonthlySalary</b> (double) — обязательный, средняя зарплата за 12 месяцев</li>
 *   <li><b>daysCount</b> (Integer) — необязательный, количество дней отпуска</li>
 *   <li><b>vacationDates</b> (List&lt;LocalDate&gt;) — необязательный, конкретные даты отпуска
 *       (по умолчанию игнорируются выходные/праздничные дни)</li>
 * </ul>
 * Применяется валидация JSR‑303 (поля и class‑level через {@code @Valid})
 * В случае ошибок валидации возвращает <code>400 Bad Request</code> с деталями
 * Бизнес‑ошибки превращаются в соответствующие http‑статусы (см. {@code GlobalExceptionHandler})
 * </p>
 */
@RestController
@Validated
@RequestMapping("/calculate")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @GetMapping
    public VacationCalculationResponse calculate(@Valid @ModelAttribute VacationCalculationRequest req) {
        return vacationService.calculate(req);
    }
}