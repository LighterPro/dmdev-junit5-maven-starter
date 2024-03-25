package com.dmdev.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesUtilTest {

    @ParameterizedTest
    @MethodSource("provideKnownPropertiesWithExpectedValues")
    void shouldGetPropertyValueCorrectly(String key, String expected) {
        String actual = PropertiesUtil.get(key);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> provideKnownPropertiesWithExpectedValues() {
        return Stream.of(
                Arguments.of("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
                Arguments.of("db.user", "sa"),
                Arguments.of("db.password", "")
        );
    }
}