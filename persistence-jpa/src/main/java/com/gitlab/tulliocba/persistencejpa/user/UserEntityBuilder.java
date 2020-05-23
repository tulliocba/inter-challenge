package com.gitlab.tulliocba.persistencejpa.user;

import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.common.builder.Builder;
import com.gitlab.tulliocba.persistencejpa.uniquenumber.UniqueNumberEntity;
import com.gitlab.tulliocba.persistencejpa.uniquenumber.UniqueNumberEntityBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

import static com.gitlab.tulliocba.common.constants.Status.ACTIVE;
import static java.util.stream.Collectors.toSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntityBuilder implements Builder<UserEntity> {

    private User user;

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    public UserEntityBuilder given(User user) {
        this.user = user;
        return this;
    }

    @Override
    public UserEntity build() {
        final UserEntity entity = new UserEntity();
        Objects.requireNonNull(user, "User cannot be null");

        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setUuid(user.getUuid().getValue());
        entity.setStatus(ACTIVE);
        entity.setPublicKey(user.getPublicKey());
        entity.setUniqueNumbers(mapUniqueNumberDomainToEntity());
        return entity;
    }

    private Set<UniqueNumberEntity> mapUniqueNumberDomainToEntity() {
        return user.getUniqueNumbers().stream()
                .map(uniqueNumber -> UniqueNumberEntityBuilder
                        .builder().given(uniqueNumber).build()).collect(toSet());
    }
}
