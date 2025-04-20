package com.example.vacationcalculator.client;

import com.example.vacationcalculator.config.HolidayApiProperties;
import com.example.vacationcalculator.exception.BusinessErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Set;

/**
 * Клиент для получения списка праздничных дней через внешний Holiday API
 * <p>
 * Формирует http‑запрос с параметрами страны (в нашем случае это ru), года и api‑ключа, заданными
 * в {@link HolidayApiProperties}, и разбирает ответ в {@link HolidayApiResponse}
 * Все ошибки взаимодействия с api (сетевая ошибка, пустой ответ, сбой парсинга
 * дат) оборачиваются в {@link BusinessErrorException} с соответствующим кодом
 * </p>
 */
@Service
@RequiredArgsConstructor
public class HolidayApiClient implements HolidayClient {
    private final RestTemplate restTemplate;
    private final HolidayApiProperties props;

    @Override
    public Set<LocalDate> getHolidays(int year) {
        String url = "%s?country=RU&year=%d&key=%s"
                .formatted(props.url(), year, props.apiKey());

        HolidayApiResponse resp;
        try {
            resp = restTemplate.getForObject(url, HolidayApiResponse.class);
        } catch (RestClientException ex) {
            throw new BusinessErrorException(
                    "HOLIDAY_API_CALL_ERROR",
                    "Не удалось обратиться к Holiday API для года " + year + ": " + ex.getMessage()
            );
        }

        if (resp == null || resp.getHolidays() == null) {
            throw new BusinessErrorException(
                    "HOLIDAY_API_EMPTY_RESPONSE",
                    "Праздники не найдены для года " + year
            );
        }

        try {
            return resp.toLocalDates();
        } catch (DateTimeException ex) {
            throw new BusinessErrorException(
                    "HOLIDAY_API_PARSE_ERROR",
                    "Не удалось распарсить дату из ответа Holiday API для года " + year + ": " + ex.getMessage()
            );
        }
    }
}