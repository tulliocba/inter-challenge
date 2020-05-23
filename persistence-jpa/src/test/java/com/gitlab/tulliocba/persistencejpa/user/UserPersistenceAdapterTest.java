package com.gitlab.tulliocba.persistencejpa.user;

import com.gitlab.tulliocba.application.unique_number.domain.Param;
import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.domain.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UserPersistenceAdapter.class)
public class UserPersistenceAdapterTest {

    public static final String USER_ID = "3fc0313d-9217-4543-9788-7530ac3a0d8e";
    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    void should_succeed_saving_user() {
        final User user = UserBuilder.builder()
                .given(UUID.randomUUID().toString(), "João", "user@gmail.com").build();
        final User savedUser = userPersistenceAdapter.save(user);

        assertThat(user).isEqualTo(savedUser);
    }

    @Test
    @Sql("classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_finding_user_by_uuid() {
        final Optional<User> userDB = userPersistenceAdapter.findUserById(USER_ID);

        assertThat(userDB.isPresent()).isTrue();

        userDB.ifPresent(user -> assertThat(user.getUuid().getValue()).isEqualTo(USER_ID));
    }


    @Test
    @Sql("classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_deleting_user() {
        userPersistenceAdapter.deleteUserById(USER_ID);

        final Optional<User> user = userPersistenceAdapter.findUserById(USER_ID);

        assertThat(user.isPresent()).isFalse();
    }

    @Test
    @Sql("classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_adding_an_unique_number() {
        final Optional<User> userDB = userPersistenceAdapter.findUserById(USER_ID);

        final UniqueNumber uniqueNumber = UniqueNumber.from(Param.of("9875", 4));

        userDB.ifPresent(user -> {
            user.addUniqueNumber(uniqueNumber);
            userPersistenceAdapter.save(user);
        });

        final Optional<User> userUpdated = userPersistenceAdapter.findUserById(USER_ID);

        assertThat(userUpdated.isPresent()).isTrue();

        userUpdated.ifPresent(user -> assertThat(user.getUniqueNumbers()).isNotEmpty());
    }
}
