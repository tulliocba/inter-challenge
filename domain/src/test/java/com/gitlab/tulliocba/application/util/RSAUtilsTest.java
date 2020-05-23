package com.gitlab.tulliocba.application.util;

import com.gitlab.tulliocba.common.util.RSAKeyUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.spec.InvalidKeySpecException;

import static org.assertj.core.api.Assertions.assertThat;

public class RSAUtilsTest {

    @Test
    void should_succeed_encrypting_and_decrypting() throws InvalidKeySpecException {
        final String name = "Tuio Gabriel";

        final String encryptedName = RSAUtils.encrypt(name, RSAKeyUtils.getPublicKey());

        assertThat(name).isNotEqualTo(encryptedName);

        final String decryptName = RSAUtils.decrypt(encryptedName, RSAKeyUtils.getPrivate());

        assertThat(name).isEqualTo(decryptName);
    }

    @Test
    void should_fail_when_the_public_key_is_invalid() {

        Assertions.assertThrows(InvalidKeySpecException.class, () -> {
            final String name = "Tuio Gabriel";

            RSAUtils.encrypt(name, "public key");
        });

    }
}
