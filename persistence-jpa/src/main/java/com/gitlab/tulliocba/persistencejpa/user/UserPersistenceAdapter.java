package com.gitlab.tulliocba.persistencejpa.user;

import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {

    private final UserJpaRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(UserEntityBuilder.builder().given(user).build()).toDomain();

    }

    @Override
    public Optional<User> findUserById(String id) {
        return userRepository.findUserEntityByUuid(id)
                .map(UserEntity::toDomain);
    }


    @Override
    public void deleteUserById(String id) {
        userRepository.deleteByUuid(id);
    }
}
