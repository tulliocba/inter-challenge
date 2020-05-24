package com.gitlab.tulliocba.application.user.service;

import com.gitlab.tulliocba.application.user.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface SendPublicKeyUseCase {

    Optional<User> sendPublicKey(String userId, String publicKey);

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @ApiModel(description = "Model to send a new public key")
    class NewPublicKeyCommand {

        @NotBlank
        private String publicKey;
    }
}
