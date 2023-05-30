package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.user.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.application.data.mapper.UserDTOUserModelMapper;
import com.elysium.reddot.ms.user.application.service.UserApplicationServiceImpl;
import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import com.elysium.reddot.ms.user.infrastructure.constant.UserRouteEnum;
import com.elysium.reddot.ms.user.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.user.CreateUserProcessor;
import com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.user.UserProcessorHolder;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRouteBuilderTest extends CamelTestSupport {

    @Mock
    private UserApplicationServiceImpl userApplicationService;

    @Override
    protected RouteBuilder createRouteBuilder() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        UserProcessorHolder userProcessorHolder = new UserProcessorHolder(
                new CreateUserProcessor(userApplicationService)
        );

        return new UserRouteBuilder(globalExceptionHandler, userProcessorHolder);
    }


    @Test
    @DisplayName("given valid user when route createUser is called then user created")
    void givenValidUser_whenRouteCreateUser_thenUserCreated() {
        // given
        UserDTO userDTO = new UserDTO(null, "username", "Pass0rd&", "mail@gmail", true);
        UserModel userModel = UserDTOUserModelMapper.toUserModel(userDTO);
        UserModel userCreatedModel = new UserModel("id", "username", "email@mail", true, "Pass0rd&");
        UserDTO userCreatedDTO = UserDTOUserModelMapper.toUserDTO(userCreatedModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(userDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "User created successfully.", userCreatedDTO);

        // mock
        when(userApplicationService.createUser(userModel)).thenReturn(userCreatedModel);

        // when
        Exchange responseExchange = template.send(UserRouteEnum.USER_REGISTRATION.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given createUser service failure when route is called then GlobalException is triggered with badRequestException")
    void givenCreateUserServiceFailure_whenRouteIsCalled_thenGlobalExceptionIsTriggeredWithBadRequestException() {

        // given
        UserDTO userDTO = new UserDTO("xxx", "username", "Pa", "mail@gmail", true);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(userDTO);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("BadValueException", "Bad value in password : must have a minimum length 8. Password has 2");

        // when
        when(userApplicationService.createUser(any(UserModel.class)))
                .thenThrow(new BadValueException("password", "must have a minimum length 8. Password has 2"));

        Exchange responseExchange = template.send(UserRouteEnum.USER_REGISTRATION.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getIn().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(400,responseExchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class));
    }

    @Test
    @DisplayName("given createUser service failure when route is called then GlobalException is triggered with nullPointerException")
    void givenCreateUserServiceFailure_whenRouteIsCalled_thenGlobalExceptionIsTriggeredWithNullPointerException() {

        // given
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(null);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("NullPointerException", null);

        // when
        Exchange responseExchange = template.send(UserRouteEnum.USER_REGISTRATION.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getIn().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(500,responseExchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class));
    }


}
