package com.gitlab.tulliocba.application.unique_number.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UniqueNumberTest {

    @Test
    void should_instantiate_a_unique_number() {
        final UniqueNumber uniqueNumber = UniqueNumber.from(Param.of("8", 1));
        Assertions.assertThat(uniqueNumber).isNotNull();
    }

    @Test
    void should_check_unique_number_value() {
        final int value = UniqueNumber.from(Param.of("8", 1)).getValue();
        Assertions.assertThat(value).isEqualTo(8);
    }
}
