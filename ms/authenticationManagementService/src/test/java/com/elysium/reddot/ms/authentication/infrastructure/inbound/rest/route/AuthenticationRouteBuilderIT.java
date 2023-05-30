package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.LoginRequestDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.UserAuthenticatedDTO;
import com.elysium.reddot.ms.authentication.container.TestContainerSetup;
import com.elysium.reddot.ms.authentication.infrastructure.data.exception.GlobalExceptionDTO;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthenticationRouteBuilderIT extends TestContainerSetup {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @Test
    @DisplayName("given Exchange with LoginRequestDTO, when LoginProcessor is invoked, then authentication is performed")
    void givenExchangeWithLoginRequestDTO_whenLoginProcessorInvoked_thenAuthenticationPerformed() throws Exception {
        //given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user1", "test");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/login");
        exchange.getIn().setBody(loginRequestDTO);


        // when
        Exchange responseExchange = template.send(AuthenticationRouteEnum.LOGIN.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(200, "Authenticated with name user1 successfully", actualResponse.getData());


        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("Given Exchange with LoginRequestDTO, when LoginProcessor is invoked and an exception is thrown, then appropriate ApiResponseDTO is set in Exchange message")
    void givenExchangeWithLoginRequestDTO_whenLoginProcessorInvokedAndExceptionThrown_thenAppropriateApiResponseDTOSetInExchangeMessage() throws Exception {
        // given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("xxx", "password");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/login");
        exchange.getIn().setBody(loginRequestDTO);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("KeycloakApiException",
                "Error Keycloak API : invalid_grant -> Invalid user credentials");

        // when
        Exchange responseExchange = template.send(AuthenticationRouteEnum.LOGIN.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getIn().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(500, responseExchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class));
    }

    @Test
    @DisplayName("given exchange with accessToken, when LogoutProcessor is invoked, then appropriate ApiResponseDTO is set in Exchange message")
    void givenExchangeWithAccessToken_whenLogoutProcessorInvoked_thenAppropriateApiResponseDTOSetInExchangeMessage() {
        // Given
        UserAuthenticatedDTO userAuthenticatedDTO = getLogin();

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/logout");
        exchange.getIn().setBody(userAuthenticatedDTO);

        // when
        Exchange responseExchange = template.send(AuthenticationRouteEnum.LOGOUT.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(200, "You have been logged out successfully.", actualResponse.getData());

        // then
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    private UserAuthenticatedDTO getLogin() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user1", "test");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/login");
        exchange.getIn().setBody(loginRequestDTO);

        Exchange responseExchange = template.send(AuthenticationRouteEnum.LOGIN.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        AccessTokenResponse token = (AccessTokenResponse) actualResponse.getData();

        return new UserAuthenticatedDTO(token.getToken());
    }


}
