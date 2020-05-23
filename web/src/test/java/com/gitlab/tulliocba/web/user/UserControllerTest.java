package com.gitlab.tulliocba.web.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.domain.UserBuilder;
import com.gitlab.tulliocba.application.user.service.SendPublicKeyUseCase;
import com.gitlab.tulliocba.application.user.service.SendPublicKeyUseCase.NewPublicKeyCommand;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.NewUserCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static com.gitlab.tulliocba.application.user.domain.UserBuilder.builder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    public static final String UTF_8 = "UTF-8";
    public static final String API_USERS = "/api/users/";
    private static final String USER_NAME = "João";
    private static final String USER_EMAIL = "joao@gmail.com";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCRUDUseCase userCRUDUseCase;

    @MockBean
    private SendPublicKeyUseCase sendPublicKeyUseCase;

    @Test
    void should_succeed_creating_new_user() throws Exception {
        NewUserCommand command = new NewUserCommand(USER_NAME, USER_EMAIL);

        doReturn(UserBuilder.builder().given(command.getName(), command.getEmail()).build())
                .when(userCRUDUseCase).createUser(any());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8)
                .content(new ObjectMapper().writeValueAsBytes(command));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void should_succeed_updating_user() throws Exception {
        final String id = UUID.randomUUID().toString();
        NewUserCommand command = new NewUserCommand(USER_NAME, USER_EMAIL);

        doReturn(Optional.of(builder().given(id, command).build()))
                .when(userCRUDUseCase).updateUser(any(), any());

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put(API_USERS + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(new ObjectMapper().writeValueAsBytes(command));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void should_succeed_retrieving_user() throws Exception {
        doReturn(Optional.of(builder().given(USER_NAME, USER_EMAIL).build()))
                .when(userCRUDUseCase).getUserById(any());

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(API_USERS + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void should_succeed_deleting_user() throws Exception {
        doNothing().when(userCRUDUseCase).deleteUser(any());

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete(API_USERS + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void should_succeed_sending_public_key() throws Exception {
        final Optional<User> user = Optional.of(builder()
                .given(USER_NAME, USER_EMAIL).build());

        doReturn(user).when(sendPublicKeyUseCase).sendPublicKey(any(), any());

        final NewPublicKeyCommand command = new NewPublicKeyCommand("public key");

        final String url = API_USERS + UUID.randomUUID().toString() + "/keys";

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(new ObjectMapper().writeValueAsBytes(command));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void should_succeed_retrieving_unique_numbers() throws Exception {
        doReturn(new HashSet<>()).when(userCRUDUseCase).findAllUniqueNumbers(any());

        final String url = API_USERS + UUID.randomUUID().toString() + "/uniqueNumbers";

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
