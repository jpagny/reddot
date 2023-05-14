package com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.user.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.application.data.mapper.UserDTOUserModelMapper;
import com.elysium.reddot.ms.user.application.service.UserApplicationServiceImpl;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CreateUserProcessorTest {

    private CreateUserProcessor createUserProcessor;
    private CamelContext camelContext;

    @Mock
    private UserApplicationServiceImpl userApplicationService;


    @BeforeEach
    public void setup() {
        camelContext = new DefaultCamelContext();
        createUserProcessor = new CreateUserProcessor(userApplicationService);
    }

    @Test
    @DisplayName("Given a valid Exchange, when process is called, then it should set the response correctly")
    public void givenValidExchange_whenProcessCalled_thenShouldSetResponseCorrectly() {
        UserDTO userDTO = new UserDTO(null, "username", "Pass0rd&", "mail@gmail", true);
        UserModel userModel = UserDTOUserModelMapper.toUserModel(userDTO);
        UserModel userCreatedModel = new UserModel("id", "username", "email@mail", true, "Pass0rd&");
        UserDTO expectedUser = UserDTOUserModelMapper.toUserDTO(userCreatedModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "User created successfully.", userCreatedModel);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/register");
        exchange.getIn().setBody(userDTO);

        // mock
        when(userApplicationService.createUser(userModel)).thenReturn(userCreatedModel);

        // when
        createUserProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }
}
