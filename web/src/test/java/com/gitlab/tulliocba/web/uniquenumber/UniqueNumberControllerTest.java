package com.gitlab.tulliocba.web.uniquenumber;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.tulliocba.application.unique_number.domain.Param;
import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.NewUniqueNumberCalculationCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UniqueNumberController.class)
public class UniqueNumberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculateUniqueNumberUserCase calculateUniqueNumberUserCase;

    @Test
    void should_succeed_requesting_new_unique_number() throws Exception {
        final String number = "9875";
        final int concatenationNumber = 4;
        NewUniqueNumberCalculationCommand command =
                new NewUniqueNumberCalculationCommand(concatenationNumber, number, null);

        final UniqueNumber calculatedNumber = UniqueNumber.calculated(Param.of(number, concatenationNumber), 8);

        doReturn(calculatedNumber).when(calculateUniqueNumberUserCase).calculateUniqueNumber(any());

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/uniqueNumbers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsBytes(command));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
