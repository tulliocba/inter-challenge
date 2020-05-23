package com.gitlab.tulliocba.application.unique_number.service;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.NewUniqueNumberCalculationCommand;
import com.gitlab.tulliocba.application.user.domain.UserBuilder;
import com.gitlab.tulliocba.application.user.infrastructure.UserRepository;
import com.gitlab.tulliocba.application.user.service.exception.RuleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UniqueNumberServiceTest {

    @Mock
    private UserRepository userRepository;

    private UniqueNumberService service;

    @BeforeEach
    void setUp() {
        service = new UniqueNumberService(userRepository);
    }

    @Test
    void should_fail_when_request_unique_number_with_invalid() {
        final String id = UUID.randomUUID().toString();
        final NewUniqueNumberCalculationCommand command =
                new NewUniqueNumberCalculationCommand(4, "9875", id);

        doReturn(Optional.empty()).when(userRepository).findUserById(id);

        Assertions.assertThrows(RuleException.class, () -> service.calculateUniqueNumber(command));
    }

    @Test
    void should_succeed_requesting_unique_number_without_userId() {
        final NewUniqueNumberCalculationCommand command =
                new NewUniqueNumberCalculationCommand(4, "9875");

        final UniqueNumber uniqueNumber = service.calculateUniqueNumber(command);

        verify(userRepository, times(0)).findUserById(any());
        verify(userRepository, times(0)).save(any());

        assertThat(uniqueNumber.getValue()).isEqualTo(8);
    }


    @Test
    void should_succeed_requesting_unique_number_with_valid_userId() {
        final String id = UUID.randomUUID().toString();
        final NewUniqueNumberCalculationCommand command =
                new NewUniqueNumberCalculationCommand(4, "9875", id);

        doReturn(Optional.of(UserBuilder.builder()
                .given(id, "João", "joao@gmail.com").build()))
                .when(userRepository).findUserById(id);

        final UniqueNumber uniqueNumber = service.calculateUniqueNumber(command);

        assertThat(uniqueNumber.getValue()).isEqualTo(8);
    }
}
