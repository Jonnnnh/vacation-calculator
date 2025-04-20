package com.example.vacationcalculator.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.*;

class PropertiesBindingTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withPropertyValues(
                    "vacation-calculator.pay.day-divisor=28.5",
                    "vacation-calculator.holiday-api.url=https://api",
                    "vacation-calculator.holiday-api.api-key=KEY"
            )
            .withUserConfiguration(VacationPayProperties.class, HolidayConfig.class);

    @Test
    void propsBindCorrectly() {
        runner.run(ctx -> {
            VacationPayProperties pay = ctx.getBean(VacationPayProperties.class);
            HolidayApiProperties holiday = ctx.getBean(HolidayApiProperties.class);
            assertThat(pay.getDayDivisor()).isEqualTo(28.5);
            assertThat(holiday.url()).isEqualTo("https://api");
            assertThat(holiday.apiKey()).isEqualTo("KEY");
        });
    }
}