package com.adobe.RomanNumerals.service;

import io.micrometer.core.instrument.MockClock;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RomanConverterTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/arabic_roman_mapping.csv", numLinesToSkip = 1)
    void convertArabicToRomanNumbers(int input, String expected) {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, new MockClock());
        RomanConverter underTest = new RomanConverter(meterRegistry);
        Assertions.assertThat(underTest.convertFromArabic(input)).isEqualTo(expected);
        Assertions.assertThat(meterRegistry.counter("roman.count", "number", String.valueOf(input)).count()).isEqualTo(1.0);
    }

    @Test
    void testTooSmallNumberExc() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, new MockClock());
        IllegalArgumentException exc = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                new RomanConverter(meterRegistry).convertFromArabic(0));
        Assertions.assertThat(exc).hasMessage("Provided number: 0 is lower than 1");
    }

    @Test
    void testTooBigNumberExc() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, new MockClock());
        IllegalArgumentException exc = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                new RomanConverter(meterRegistry).convertFromArabic(256));
        Assertions.assertThat(exc).hasMessage("Provided number: 256 is greater than 255");
    }

}