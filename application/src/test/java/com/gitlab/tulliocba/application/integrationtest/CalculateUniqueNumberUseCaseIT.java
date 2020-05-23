package com.gitlab.tulliocba.application.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.NewUniqueNumberCalculationCommand;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.UniqueNumberResultView;
import com.gitlab.tulliocba.application.user.domain.User;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalculateUniqueNumberUseCaseIT {

    public static final String API_UNIQUE_NUMBERS = "/api/uniqueNumbers";
    public static final String USER_ID = "3fc0313d-9217-4543-9788-7530ac3a0d8e";

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
        RestAssured.basePath = API_UNIQUE_NUMBERS;
    }

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAll();
    }

    @Test
    void should_fail_requesting_new_unique_number_with_invalid_userId() throws JsonProcessingException {
        final String number = "9875";
        final int concatenationNumber = 4;

        NewUniqueNumberCalculationCommand command =
                new NewUniqueNumberCalculationCommand(concatenationNumber, number, USER_ID);


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsBytes(command))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .extract();
    }

    @Test
    @Sql("classpath:scripts/test/create_complete_user_test.sql")
    void should_succeed_requesting_new_unique_number_with_userId() throws JsonProcessingException {
        final String number = "9875";
        final int concatenationNumber = 4;
        NewUniqueNumberCalculationCommand command =
                new NewUniqueNumberCalculationCommand(concatenationNumber, number, USER_ID);

        final UniqueNumberResultView uniqueNumberResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsBytes(command))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .extract().as(UniqueNumberResultView.class);

        final Optional<User> userDB = userPersistenceAdapter.findUserById(USER_ID);

        assertThat(userDB.isPresent()).isTrue();

        userDB.ifPresent(user -> {
            assertThat(user.getUniqueNumbers()).isNotEmpty();
            final UniqueNumber uniqueNumber = user.getUniqueNumbers().iterator().next();
            assertThat(uniqueNumber.getValue()).isEqualTo(8);

        });

        assertThat(uniqueNumberResponse.getUniqueNumber()).isEqualTo(8);
    }

    @Test
    void should_succeed_requesting_new_unique_number_without_userId() throws JsonProcessingException {
        final String number = "9875";
        final int concatenationNumber = 4;
        NewUniqueNumberCalculationCommand command =
                new NewUniqueNumberCalculationCommand(concatenationNumber, number);

        final UniqueNumberResultView uniqueNumber = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsBytes(command))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .extract().as(UniqueNumberResultView.class);

        assertThat(uniqueNumber.getUniqueNumber()).isEqualTo(8);
    }
}
