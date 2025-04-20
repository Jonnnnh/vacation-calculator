package com.example.vacationcalculator.service.impl;

import com.example.vacationcalculator.dto.VacationCalculationRequest;
import com.example.vacationcalculator.dto.VacationCalculationResponse;
import com.example.vacationcalculator.exception.BusinessErrorException;
import com.example.vacationcalculator.exception.ValidationException;
import com.example.vacationcalculator.service.VacationPayCalculator;
import com.example.vacationcalculator.service.VacationService;
import com.example.vacationcalculator.service.BusinessDaysCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * {@link VacationServiceImpl} — сервис, отвечающий за полную бизнес-логику расчёта отпускных
 * <p>
 * Выполняет следующие шаги:
 * <ol>
 *   <li>Валидация входных данных (минимальная месячная зарплата)</li>
 *   <li>Определение количества рабочих дней отпуска:
 *       <ul>
 *         <li>если передан список дат — вычисление через {@link BusinessDaysCalculator}</li>
 *         <li>если передано только число дней — использование его напрямую</li>
 *       </ul>
 *   </li>
 *   <li>Проверка бизнес‑правила: не более 30 рабочих дней за один раз</li>
 *   <li>Вычисление суммы отпускных через {@link VacationPayCalculator}</li>
 *   <li>Логирование ключевых этапов и результатов расчёта</li>
 * </ol>
 * Бросает:
 * <ul>
 *   <li>{@link ValidationException} — если средняя зарплата < 1000</li>
 *   <li>{@link BusinessErrorException} — если рассчитанных рабочих дней > 30</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VacationServiceImpl implements VacationService {
    private final BusinessDaysCalculator daysCounter;
    private final VacationPayCalculator payCalc;

    @Override
    public VacationCalculationResponse calculate(VacationCalculationRequest req) {
        log.debug("Старт расчёта отпускных: {}", req);

        if (req.getAverageMonthlySalary() < 1000) {
            log.debug("Ошибка валидации: averageMonthlySalary={} < 1000",
                    req.getAverageMonthlySalary());
            throw new ValidationException(
                    "averageMonthlySalary должно быть не менее 1000"
            );
        }

        int workingDays = (req.getVacationDates() != null && !req.getVacationDates().isEmpty())
                ? daysCounter.calculateBusinessDays(req.getVacationDates())
                : req.getDaysCount();
        log.debug("Количество рабочих дней для расчёта: {}", workingDays);

        if (workingDays > 30) {
            log.debug("Бизнес‑ошибка: рабочих дней больше 30 ({} > 30)", workingDays);
            throw new BusinessErrorException(
                    "TOO_MANY_DAYS",
                    "Нельзя рассчитать отпуск дольше чем на 30 рабочих дней"
            );
        }

        double total = payCalc.calculateVacationPay(req.getAverageMonthlySalary(), workingDays);
        log.debug("Итоговая сумма отпускных: {}", total);
        VacationCalculationResponse resp = new VacationCalculationResponse(total);
        log.debug("Завершение расчёта отпускных: {}", resp);
        return resp;
    }
}