package com.gitlab.tulliocba.application.unique_number.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UniqueNumberGeneratorTest {

    @Test
    void should_not_allow_concat_number_less_then_one() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> UniqueNumberGenerator.generateFrom(Param.of("8", 0)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> UniqueNumberGenerator.generateFrom(Param.of("8", -1)));
    }

    @Test
    void should_not_allow_concat_number_greater_then_1E5() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> UniqueNumberGenerator.generateFrom(Param.of("8", 100001)));
    }

    @Test
    void should_not_allow_negative_unique_number() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> UniqueNumberGenerator.generateFrom(Param.of("-121232323232323", 5)));
    }

    @Test
    void should_calc_unique_number_from_big_number() {
        final int value = UniqueNumberGenerator.generateFrom(Param.of("9875", 4));
        assertThat(value).isEqualTo(8);
    }

    @Test
    void should_calc_unique_number_from_small_number() {
        final int value = UniqueNumberGenerator.generateFrom(Param.of("8", 1));
        assertThat(value).isEqualTo(8);
    }
}
