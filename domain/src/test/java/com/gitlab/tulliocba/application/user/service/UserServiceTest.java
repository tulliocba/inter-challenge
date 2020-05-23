package com.gitlab.tulliocba.application.user.service;

import com.gitlab.tulliocba.application.unique_number.domain.Param;
import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.domain.UserBuilder;
import com.gitlab.tulliocba.application.user.infrastructure.UserRepository;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.NewUserCommand;
import com.gitlab.tulliocba.application.user.service.exception.RuleException;
import com.gitlab.tulliocba.application.util.RSAUtils;
import com.gitlab.tulliocba.common.util.RSAKeyUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.StringUtils.isNotBlank;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    public static final String USER_EMAIL = "joao@gmail.com";
    public static final String USER_NAME = "João";
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void should_succeed_creating_user() {
        final User user = UserBuilder.builder().given(USER_NAME, USER_EMAIL).build();
        final NewUserCommand command = new NewUserCommand(USER_NAME, USER_EMAIL);

        doReturn(user).when(userRepository).save(any());

        final User userSaved = userService.createUser(command);

        verify(userRepository).save(any());

        assertThat(user.getName()).isEqualTo(userSaved.getName());
        assertThat(user.getEmail()).isEqualTo(userSaved.getEmail());
        assertThat(isNotBlank(userSaved.getUuid().getValue())).isTrue();
    }

    @Test
    void should_fail_retrieving_user_when_data_are_decrypted() {
        Assertions.assertThrows(RuleException.class, () -> {
            final String id = UUID.randomUUID().toString();
            final User user = buildUser(id);

            doReturn(Optional.of(user)).when(userRepository).findUserById(id);

            userService.getUserById(id);
            verify(userRepository).findUserById(id);
        });
    }


    @Test
    void should_succeed_retrieving_user() {
        final String id = UUID.randomUUID().toString();
        final User builtUser = buildUser(id);
        builtUser.addKey(RSAKeyUtils.getPublicKey());

        doReturn(Optional.of(builtUser)).when(userRepository).findUserById(id);

        final Optional<User> userDB = userService.getUserById(id);

        verify(userRepository).findUserById(id);

        assertThat(userDB.isPresent()).isTrue();

        userDB.ifPresent(user -> assertThat(user).isEqualTo(builtUser));
    }

    @Test
    void should_succeed_updating_user() {
        final String id = UUID.randomUUID().toString();
        final User builtEncryptedUser = buildEncryptedUser(id);

        final NewUserCommand command = new NewUserCommand("Carlos", "carlos@gmail.com");

        doReturn(Optional.of(builtEncryptedUser)).when(userRepository).findUserById(id);

        final User builtUser = UserBuilder.builder().given(id, command.getName(), command.getEmail()).build();
        builtUser.addKey(RSAKeyUtils.getPublicKey());

        doReturn(builtUser)
                .when(userRepository).save(any());

        final Optional<User> updatedUserDB = userService.updateUser(id, command);

        verify(userRepository).findUserById(id);
        verify(userRepository).save(any());

        assertThat(updatedUserDB.isPresent()).isTrue();

        updatedUserDB.ifPresent(user -> {
            assertThat(RSAUtils.decrypt(user.getName(), RSAKeyUtils.getPrivate())).isEqualTo("Carlos");
            assertThat(user.getUuid().getValue()).isEqualTo(id);
        });


    }

    @Test
    void should_succeed_deleting_user() {
        final String id = UUID.randomUUID().toString();

        userService.deleteUser(id);

        verify(userRepository).deleteUserById(id);
    }

    @Test
    void should_succeed_sending_public_key() {
        final String id = UUID.randomUUID().toString();
        final User user = buildUser(id);

        doReturn(Optional.of(user)).when(userRepository).findUserById(id);
        final User encryptedUser = buildEncryptedUser(id);
        doReturn(encryptedUser).when(userRepository).save(user);

        userService.sendPublicKey(id, RSAKeyUtils.getPublicKey());

        assertThat(user.getName()).isNotEqualTo(encryptedUser.getName());
        assertThat(user.getEmail()).isNotEqualTo(encryptedUser.getEmail());
    }

    @Test
    void should_fail_when_try_to_find_unique_numbers_for_invalid_userId() {
        doReturn(Optional.empty()).when(userRepository).findUserById(any());

        Assertions.assertThrows(RuleException.class, () ->
                userService.findAllUniqueNumbers(UUID.randomUUID().toString()));
    }

    @Test
    void should_succeed_finding_unique_numbers_by_user() {
        final String id = UUID.randomUUID().toString();
        final User user = buildEncryptedUser(id);

        doReturn(Optional.of(user)).when(userRepository).findUserById(id);

        final Set<UniqueNumber> allUniqueNumbers = userService.findAllUniqueNumbers(id);

        assertThat(allUniqueNumbers).isNotEmpty();
    }

    private User buildUser(String id) {
        return UserBuilder.builder()
                .given(id, USER_NAME, USER_EMAIL).build();
    }

    private User buildEncryptedUser(String id) {
        final User user = UserBuilder.builder()
                .given(1L, id, USER_NAME,
                        "user@gmail.com", RSAKeyUtils.getPublicKey(),
                        singleton(UniqueNumber.calculated(Param.of("9875", 4), 8)))
                .build();
        user.encryptData();
        return user;
    }
}
