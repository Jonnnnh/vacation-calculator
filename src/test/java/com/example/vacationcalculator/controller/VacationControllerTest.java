package com.example.vacationcalculator.controller;

import com.example.vacationcalculator.dto.VacationCalculationResponse;
import com.example.vacationcalculator.exception.BusinessErrorException;
import com.example.vacationcalculator.exception.GlobalExceptionHandler;
import com.example.vacationcalculator.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VacationControllerStandaloneTest {

    private MockMvc mvc;

    @Mock
    private VacationService service;

    @InjectMocks
    private VacationController controller;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void validDaysCount_returns200() throws Exception {
        when(service.calculate(any()))
                .thenReturn(new VacationCalculationResponse(123.45));
        mvc.perform(get("/calculate")
                        .param("averageMonthlySalary", "50000")
                        .param("daysCount", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.totalAmount").value(123.45));
    }

    @Test
    void missingSalary_returns400() throws Exception {
        mvc.perform(get("/calculate")
                        .param("daysCount", "5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.averageMonthlySalary")
                        .value("averageMonthlySalary обязательно"));
    }

    @Test
    void bothParams_returns400() throws Exception {
        mvc.perform(get("/calculate")
                        .param("averageMonthlySalary", "50000")
                        .param("daysCount", "5")
                        .param("vacationDates", "2025-03-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.eitherDaysCountOrDates")
                        .value("Укажите либо daysCount, либо vacationDates, но не оба сразу"));
    }

    @Test
    void businessError_returns422() throws Exception {
        when(service.calculate(any()))
                .thenThrow(new BusinessErrorException("TOO_MANY_DAYS", "Слишком много дней"));
        mvc.perform(get("/calculate")
                        .param("averageMonthlySalary", "50000")
                        .param("daysCount", "40"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("TOO_MANY_DAYS"));
    }
}