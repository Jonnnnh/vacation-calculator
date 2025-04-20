package com.example.vacationcalculator.client;

import com.example.vacationcalculator.config.HolidayApiProperties;
import com.example.vacationcalculator.exception.BusinessErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.anything;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class HolidayApiClientTest {

    private HolidayApiClient client;
    private MockRestServiceServer server;

    @BeforeEach
    void init() {
        RestTemplate rest = new RestTemplate();
        server = MockRestServiceServer.createServer(rest);
        HolidayApiProperties props = new HolidayApiProperties("https://test", "KEY123");
        client = new HolidayApiClient(rest, props);
    }

    @Test
    void validResponse_parsedCorrectly() {
        String json = """
            {"holidays":[{"name":"H","date":"2025-01-01"}]}
            """;
        server.expect(requestTo("https://test?country=RU&year=2025&key=KEY123"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        Set<LocalDate> hol = client.getHolidays(2025);
        assertTrue(hol.contains(LocalDate.of(2025,1,1)));
    }

    @Test
    void emptyBody_throws() {
        server.expect(manyTimes(), requestTo(anything()))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        assertThrows(BusinessErrorException.class, () -> client.getHolidays(2025));
    }

    @Test
    void badStatus_throws() {
        server.expect(requestTo(anything()))
                .andRespond(withStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(BusinessErrorException.class, () -> client.getHolidays(2025));
    }

    @Test
    void parseError_throws() {
        String json = """
                {"holidays":[{"name":"H","date":"not-a-date"}]}""";
        server.expect(requestTo(anything()))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        assertThrows(BusinessErrorException.class, () -> client.getHolidays(2025));
    }
}