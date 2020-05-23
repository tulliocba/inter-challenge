package com.gitlab.tulliocba.application.user.domain;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.domain.User.UserId;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.NewUserCommand;
import com.gitlab.tulliocba.common.builder.Builder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBuilder implements Builder<User> {

    private Long id;
    private String uuid;
    private String name;
    private String email;
    private String publicKey;
    private Set<UniqueNumber> uniqueNumbers;

    public static UserBuilder builder() {
        return new UserBuilder();
    }


    @Override
    public User build() {
        return new User(id, new UserId(uuid), name, email, publicKey, uniqueNumbers);
    }

    public UserBuilder given(Long id, String uuid, String name, String email, String publicKey, Set<UniqueNumber> uniqueNumbers) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.publicKey = publicKey;
        this.uniqueNumbers = uniqueNumbers;
        return this;
    }

    public UserBuilder given(Long id, String uuid, String name, String email, String publicKey) {
        return given(id, uuid, name, email, publicKey, null);
    }

    public UserBuilder given(String uuid, String name, String email, String publicKey) {
        return given(null, uuid, name, email, publicKey);
    }

    public UserBuilder given(String uuid, String name, String email) {
        return given(uuid, name, email, null);
    }

    public UserBuilder given(String name, String email) {
        return given(null, name, email);
    }

    public UserBuilder given(String uuid, NewUserCommand command) {
        Objects.requireNonNull(command, "NewUserCommand cannot be null");
        return given(uuid, command.getName(), command.getEmail());
    }

    public UserBuilder given(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        return given(user.getId(), user.getUuid().getValue(), user.getName(), user.getEmail(), user.getPublicKey());
    }
}
