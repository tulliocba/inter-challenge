package com.gitlab.tulliocba.application.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.service.SendPublicKeyUseCase.NewPublicKeyCommand;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.NewUserCommand;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.UniqueNumberView;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.UserView;
import com.gitlab.tulliocba.application.util.RSAUtils;
import com.gitlab.tulliocba.common.util.RSAKeyUtils;
import com.gitlab.tulliocba.persistencejpa.user.UserJpaRepository;
import com.gitlab.tulliocba.persistencejpa.user.UserPersistenceAdapter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.StringUtils.isNotBlank;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserCrudUseCaseIT {

    public static final String USER_ID = "3fc0313d-9217-4543-9788-7530ac3a0d8e";
    public static final String API_USERS = "/api/users";
    public static final String PATH_ID = "/{id}";
    @LocalServerPort
    private int port;

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = API_USERS;
    }

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAll();
    }

    @Test
    void should_succeed_creating_new_user() throws JsonProcessingException {
        final NewUserCommand newUser = new NewUserCommand("João", "joao@gmail.com");

        final UserView newUserSaved = given()
                .body(new ObjectMapper().writeValueAsBytes(newUser))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .extract().as(UserView.class);

        final Optional<User> userDB = userPersistenceAdapter.findUserById(newUserSaved.getId());

        assertThat(userDB.isPresent()).isTrue();
        userDB.ifPresent(user -> assertThat(user.getUuid().getValue()).isEqualTo(newUserSaved.getId()));
    }

    @Test
    @Sql(scripts = "classpath:scripts/test/create_user_without_publicKey_test.sql")
    void should_succeed_sending_public_key() throws JsonProcessingException {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsBytes(new NewPublicKeyCommand(RSAKeyUtils.getPublicKey())))
                .pathParam("id", USER_ID)
                .when()
                .post(PATH_ID + "/keys")
                .then()
                .statusCode(HttpStatus.SC_OK);

        final Optional<User> userDB = userPersistenceAdapter.findUserById(USER_ID);

        assertThat(userDB.isPresent()).isTrue();

        userDB.ifPresent(user -> assertThat(isNotBlank(user.getPublicKey())).isTrue());
    }

    @Test
    @Sql(scripts = "classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_retrieving_user() {
        final UserView userDB = given()
                .pathParam("id", USER_ID)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(PATH_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and().extract().as(UserView.class);

        assertThat(userDB.getId()).isEqualTo(USER_ID);
    }

    @Test
    @Sql(scripts = "classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_retrieving_unique_numbers() {
        final UniqueNumberView[] uniqueNumbers = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", USER_ID)
                .when()
                .get(PATH_ID + "/uniqueNumbers")
                .then()
                .statusCode(HttpStatus.SC_OK).extract().as(UniqueNumberView[].class);

        assertThat(uniqueNumbers.length).isEqualTo(1);
        assertThat(uniqueNumbers[0].getUniqueNumberResult()).isEqualTo(8);
    }


    @Test
    @Sql(scripts = "classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_updating_user() throws JsonProcessingException {
        final String newName = "Pedro";
        final NewUserCommand updateUser = new NewUserCommand(newName, "joao@gmail.com");
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", USER_ID)
                .body(new ObjectMapper().writeValueAsBytes(updateUser))
                .when()
                .put(PATH_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and().extract().as(UserView.class);

        final Optional<User> userDB = userPersistenceAdapter.findUserById(USER_ID);

        assertThat(userDB.isPresent()).isTrue();

        userDB.ifPresent(user -> {
            final String name = RSAUtils.decrypt(user.getName(), RSAKeyUtils.getPrivate());
            assertThat(name).isEqualTo(newName);
        });


    }

    @Test
    @Sql(scripts = "classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_deleting_user() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", USER_ID)
                .when()
                .delete(PATH_ID)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        final Optional<User> user = userPersistenceAdapter.findUserById(USER_ID);

        assertThat(user.isPresent()).isFalse();
    }
}
