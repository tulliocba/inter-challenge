package com.gitlab.tulliocba.application.user.infrastructure;

import com.gitlab.tulliocba.application.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findUserById(String id);

    void deleteUserById(String id);
}
