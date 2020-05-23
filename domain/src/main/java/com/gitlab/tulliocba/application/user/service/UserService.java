package com.gitlab.tulliocba.application.user.service;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.domain.UserBuilder;
import com.gitlab.tulliocba.application.user.infrastructure.UserRepository;
import com.gitlab.tulliocba.application.user.service.exception.RuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserCRUDUseCase, SendPublicKeyUseCase {

    private final UserRepository userRepository;


    @Override
    public User createUser(NewUserCommand command) {
        return userRepository.save(UserBuilder
                .builder().given(command.getName(), command.getEmail()).build());
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findUserById(id).map(user -> {
            user.isEncrypted();
            return user;
        });
    }

    @Override
    public Optional<User> updateUser(String id, NewUserCommand command) {
        final Optional<User> userDB = userRepository.findUserById(id);

        if (!userDB.isPresent()) return Optional.empty();

        Optional<User> updatedUser = userDB.map(user -> UserBuilder.builder().given(
                user.getId(),
                user.getUuid().getValue(),
                command.getName(),
                command.getEmail(),
                user.getPublicKey()).build());

        updatedUser.ifPresent(user -> {
            user.encryptData();
            userRepository.save(user);
            user.isEncrypted();
        });

        return updatedUser;
    }

    @Override
    public Set<UniqueNumber> findAllUniqueNumbers(String id) {
        final User user = userRepository.findUserById(id)
                .orElseThrow(() -> new RuleException("User not found"));

        return user.getUniqueNumbers();
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public Optional<User> sendPublicKey(String userId, String publicKey) {
        final Optional<User> user = userRepository.findUserById(userId);

        if (!user.isPresent()) return user;

        try {
            user.ifPresent(userDB -> userDB.addKey(publicKey));
        } catch (IllegalArgumentException e) {
            throw new RuleException(e.getMessage());
        }

        return user.map(userRepository::save);
    }
}
