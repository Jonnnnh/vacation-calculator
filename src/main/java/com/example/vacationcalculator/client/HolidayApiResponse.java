package com.example.vacationcalculator.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * dto для разбора ответа от Holiday API
 * <p>
 * Содержит список объектов {@link Holiday}, полученных в json-поле "holidays"
 * Аннотация {@code @JsonIgnoreProperties(ignoreUnknown = true)} позволяет
 * безопасно пропускать незадокументированные поля в ответе api
 * </p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayApiResponse {

    private List<Holiday> holidays;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Holiday {
        private String name;
        private String date;
    }

    public Set<LocalDate> toLocalDates() {
        return holidays.stream()
                .map(h -> LocalDate.parse(h.getDate()))
                .collect(Collectors.toSet());
    }
}