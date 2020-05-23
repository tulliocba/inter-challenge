package com.gitlab.tulliocba.application.user.domain;

import com.gitlab.tulliocba.common.util.RSAKeyUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void should_fail_when_public_key_is_empty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            final User joao = UserBuilder.builder()
                    .given(UUID.randomUUID().toString(), "João", "joao@gmail.com").build();

            joao.addKey("");
        });
    }

    @Test
    void should_succeed_encrypting_data() {
        final String name = "João";
        final String email = "user@gmail.com";
        final User user = UserBuilder.builder()
                .given(UUID.randomUUID().toString(), name, email).build();

        user.addKey(RSAKeyUtils.getPublicKey());

        assertThat(user.getPublicKey()).isNotNull();
        assertThat(name).isNotEqualTo(user.getName());
        assertThat(email).isNotEqualTo(user.getEmail());
    }
}
