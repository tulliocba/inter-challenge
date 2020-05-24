package com.gitlab.tulliocba.application.user.service;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

public interface UserCRUDUseCase {

    User createUser(NewUserCommand command);

    Optional<User> getUserById(String id);

    Optional<User> updateUser(String id, NewUserCommand command);

    Set<UniqueNumber> findAllUniqueNumbers(String id);

    void deleteUser(String id);


    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @ApiModel(description = "Model to create a new User")
    class NewUserCommand {
        private String name;
        @NotBlank
        @ApiModelProperty(example = "contact@gmail.com")
        private String email;
    }

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @ApiModel(description = "User presentation model")
    class UserView {
        private String id;
        private String name;
        private String email;
        private Set<UniqueNumberView> uniqueNumbers;
    }

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @ApiModel(description = "Unique Number presentation model")
    class UniqueNumberView {
        private int concatenationQuantity;
        private String inputNumber;
        private int uniqueNumberResult;
    }
}
