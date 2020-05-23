package com.gitlab.tulliocba.application.cache.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheManagerTest {

    @Test
    void should_not_retrieve_expired_number_from_cache() {

        for (int i = 0; i < 11; i++) CacheManager.put(String.valueOf(i), i);

        assertThat(CacheManager.get("0").isPresent()).isFalse();
    }

    @Test
    void should_succeed_retrieving_alive_numbers_from_cache() {
        for (int i = 0; i < 11; i++) CacheManager.put(String.valueOf(i), i);

        for (int i = 1; i < 11; i++) assertThat(CacheManager.get(String.valueOf(i)).isPresent()).isTrue();
    }
}
