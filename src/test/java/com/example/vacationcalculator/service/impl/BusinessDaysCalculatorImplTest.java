package com.example.vacationcalculator.service.impl;

import com.example.vacationcalculator.client.HolidayClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessDaysCalculatorImplTest {

    @Mock
    private HolidayClient holidayClient;

    @InjectMocks
    private BusinessDaysCalculatorImpl calc;

    @Test
    void onlyWeekdays_noHolidays() {
        List<LocalDate> dates = List.of(
                LocalDate.of(2025, 3, 10),
                LocalDate.of(2025, 3, 11),
                LocalDate.of(2025, 3, 12),
                LocalDate.of(2025, 3, 13),
                LocalDate.of(2025, 3, 14),
                LocalDate.of(2025, 3, 15),
                LocalDate.of(2025, 3, 16)
        );
        when(holidayClient.getHolidays(2025)).thenReturn(Set.of());
        assertEquals(5, calc.calculateBusinessDays(dates));
        verify(holidayClient).getHolidays(2025);
    }

    @Test
    void excludeWeekends_onlyMarch() {
        List<LocalDate> dates = List.of(
                LocalDate.of(2025, 3, 14),
                LocalDate.of(2025, 3, 15),
                LocalDate.of(2025, 3, 16),
                LocalDate.of(2025, 3, 17)
        );
        when(holidayClient.getHolidays(2025)).thenReturn(Set.of());
        assertEquals(2, calc.calculateBusinessDays(dates));
    }

    @Test
    void excludeHolidays_inMarch() {
        LocalDate holiday = LocalDate.of(2025, 3, 8);
        LocalDate weekday = LocalDate.of(2025, 3, 10);
        List<LocalDate> dates = List.of(holiday, weekday);
        when(holidayClient.getHolidays(2025)).thenReturn(Set.of(holiday));
        assertEquals(1, calc.calculateBusinessDays(dates));
    }

    @Test
    void nullOrEmpty_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculateBusinessDays(null));
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculateBusinessDays(List.of()));
    }
}