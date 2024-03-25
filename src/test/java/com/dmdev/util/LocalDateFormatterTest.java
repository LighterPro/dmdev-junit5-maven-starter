package com.dmdev.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class LocalDateFormatterTest {

    @Test
    void shouldFormatStringToLocalDateCorrectly() {
        String date = "2020-11-28";
        LocalDate actual = LocalDateFormatter.format(date);
        assertThat(actual).isEqualTo(LocalDate.of(2020, 11, 28));
    }

    @Test
    void shouldThrowExceptionIfDateFormatIsInvalid() {
        String date = "2020-28-11";
        assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(date));
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void shouldValidateDateStringCorrectly(String date, boolean expected) {
        boolean actual = LocalDateFormatter.isValid(date);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                Arguments.of("2020-11-28", true),
                Arguments.of("01-01-2001", false),
                Arguments.of("2020-01-01 12:12:00", false),
                Arguments.of(null, false)
        );
    }
}