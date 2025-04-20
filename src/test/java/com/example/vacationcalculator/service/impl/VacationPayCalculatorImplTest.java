package com.example.vacationcalculator.service.impl;

import com.example.vacationcalculator.config.VacationPayProperties;
import com.example.vacationcalculator.exception.BusinessErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationPayCalculatorImplTest {

    @Mock
    private VacationPayProperties props;

    @InjectMocks
    private VacationPayCalculatorImpl calc;

    @Test
    void calculatesCorrectlyWithDefaultDivisor() {
        when(props.getDayDivisor()).thenReturn(29.3);
        double salary = 30000;
        int days = 10;
        double expected = Math.round((salary / 29.3) * days * 100) / 100.0;
        double actual = calc.calculateVacationPay(salary, days);
        assertEquals(expected, actual);
    }

    @Test
    void calculatesCorrectlyWithCustomDivisor() {
        when(props.getDayDivisor()).thenReturn(30.0);
        double salary = 30000;
        int days = 10;
        double expected = Math.round((salary / 30.0) * days * 100) / 100.0;
        double actual = calc.calculateVacationPay(salary, days);
        assertEquals(expected, actual);
    }

    @Test
    void negativeSalaryThrows() {
        assertThrows(BusinessErrorException.class,
                () -> calc.calculateVacationPay(-1000, 5));
    }

    @Test
    void zeroDaysThrows() {
        assertThrows(BusinessErrorException.class,
                () -> calc.calculateVacationPay(10000, 0));
    }
}