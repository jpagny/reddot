package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.LoginRequestDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.UserAuthenticatedDTO;
import com.elysium.reddot.ms.authentication.application.service.AuthenticationApplicationService;
import com.elysium.reddot.ms.authentication.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.authentication.infrastructure.exception.processor.GlobalExceptionHandler;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.IllegalStateApiException;
import com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor.AuthenticationProcessorHolder;
import com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor.LoginProcessor;
import com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor.LogoutProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.adapters.springsecurity.KeycloakAuthenticationException;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationRouteBuilderTest extends CamelTestSupport {

    @Mock
    AuthenticationApplicationService authenticationApplicationService;

    @Override
    protected RouteBuilder createRouteBuilder() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        AuthenticationProcessorHolder authenticationProcessorHolder = new AuthenticationProcessorHolder(
                new LoginProcessor(authenticationApplicationService),
                new LogoutProcessor(authenticationApplicationService)
        );

        return new AuthenticationRouteBuilder(globalExceptionHandler, authenticationProcessorHolder);
    }

    @Test
    @DisplayName("given Exchange with LoginRequestDTO, when LoginProcessor is invoked, then authentication is performed")
    void givenExchangeWithLoginRequestDTO_whenLoginProcessorInvoked_thenAuthenticationPerformed() throws Exception {
        //given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");
        AccessTokenResponse tokenResponse = new AccessTokenResponse();
        tokenResponse.setToken("<theBearerToken>");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("CamelHttpUri", "/login");
        exchange.getIn().setBody(loginRequestDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(200, "Authenticated with name username successfully", tokenResponse);

        // mock
        when(authenticationApplicationService.getAccessToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()))
                .thenReturn(tokenResponse);

        // when
        Exchange responseExchange = template.send(AuthenticationRouteEnum.LOGIN.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("Given Exchange with LoginRequestDTO, when LoginProcessor is invoked and an exception is thrown, then appropriate ApiResponseDTO is set in Exchange message")
    void givenExchangeWithLoginRequestDTO_whenLoginProcessorInvokedAndExceptionThrown_thenAppropriateApiResponseDTOSetInExchangeMessage() throws Exception {
        // given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");
        Exception exception = new Exception("exception !!!");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("CamelHttpUri", "/login");
        exchange.getIn().setBody(loginRequestDTO);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("IllegalStateApiException",
                "API error: Test. Exception message: exception !!!");

        // mock
        when(authenticationApplicationService.getAccessToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()))
                .thenThrow(new IllegalStateApiException("Test", exception));

        // when
        Exchange responseExchange = template.send(AuthenticationRouteEnum.LOGIN.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getIn().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(500, responseExchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class));
    }

    @Test
    @DisplayName("given exchange with HttpServletRequest, when LogoutProcessor is invoked, then appropriate ApiResponseDTO is set in Exchange message")
    void givenExchangeWithHttpServletRequest_whenLogoutProcessorInvoked_thenAppropriateApiResponseDTOSetInExchangeMessage() {
        // Given
        UserAuthenticatedDTO userAuthenticatedDTO = new UserAuthenticatedDTO("<baerToken>");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("CamelHttpUri", "/logout");
        exchange.getIn().setBody(userAuthenticatedDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(200, "You have been logged out successfully.", null);

        // when
        Exchange responseExchange = template.send(AuthenticationRouteEnum.LOGOUT.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("Given a KeycloakAuthenticationException, when handleAuthFailure is invoked, then throws Exception")
    void givenKeycloakAuthenticationException_whenHandleAuthFailureInvoked_thenThrowsException() throws Exception {
        // Given
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(new KeycloakAuthenticationException("Authentication failed"));

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("Exception",
                "org.keycloak.adapters.springsecurity.KeycloakAuthenticationException: Authentication failed");

        // When
        Exchange responseExchange = template.send("direct:handleAuthFailure", exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getIn().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(500, responseExchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class));
    }



}
