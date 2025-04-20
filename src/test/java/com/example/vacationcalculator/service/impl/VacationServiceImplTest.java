package com.example.vacationcalculator.service.impl;

import com.example.vacationcalculator.dto.VacationCalculationRequest;
import com.example.vacationcalculator.exception.BusinessErrorException;
import com.example.vacationcalculator.exception.ValidationException;
import com.example.vacationcalculator.service.BusinessDaysCalculator;
import com.example.vacationcalculator.service.VacationPayCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationServiceImplTest {

    @Mock
    private BusinessDaysCalculator daysCalc;

    @Mock
    private VacationPayCalculator payCalc;

    @InjectMocks
    private VacationServiceImpl service;

    private VacationCalculationRequest mkReq(double salary, Integer days, List<LocalDate> dates) {
        var r = new VacationCalculationRequest();
        r.setAverageMonthlySalary(salary);
        r.setDaysCount(days);
        r.setVacationDates(dates);
        return r;
    }

    @Test
    void salaryTooLow_throwsValidationException() {
        var req = mkReq(500, 5, null);
        assertThrows(ValidationException.class, () -> service.calculate(req));
        verifyNoInteractions(daysCalc, payCalc);
    }

    @Test
    void tooManyDays_throwsBusinessErrorException() {
        var req = mkReq(10000, 31, null);
        assertThrows(BusinessErrorException.class, () -> service.calculate(req));
        verifyNoInteractions(payCalc);
        verifyNoInteractions(daysCalc);
    }

    @Test
    void datesPath_invokesBusinessDaysThenPayCalc() {
        var dates = List.of(LocalDate.of(2025, 6, 1));
        var req = mkReq(20000, null, dates);
        when(daysCalc.calculateBusinessDays(dates)).thenReturn(2);
        when(payCalc.calculateVacationPay(20000, 2)).thenReturn(1364.53);
        var resp = service.calculate(req);
        assertEquals(1364.53, resp.getTotalAmount());
        verify(daysCalc).calculateBusinessDays(dates);
        verify(payCalc).calculateVacationPay(20000, 2);
    }

    @Test
    void daysCountPath_invokesPayCalcOnly() {
        var req = mkReq(20000, 3, null);
        when(payCalc.calculateVacationPay(20000, 3)).thenReturn(2047.11);
        var resp = service.calculate(req);
        assertEquals(2047.11, resp.getTotalAmount());
        verify(payCalc).calculateVacationPay(20000, 3);
        verifyNoInteractions(daysCalc);
    }
}