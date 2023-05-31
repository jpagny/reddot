package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.LoginRequestDTO;
import com.elysium.reddot.ms.authentication.application.service.AuthenticationApplicationService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginProcessorTest {

    private LoginProcessor loginProcessor;

    @Mock
    private AuthenticationApplicationService authenticationApplicationService;

    private CamelContext camelContext;

    @BeforeEach
    void setup() {
        camelContext = new DefaultCamelContext();
        loginProcessor = new LoginProcessor(authenticationApplicationService);
    }

    @Test
    @DisplayName("Given Exchange with LoginRequestDTO, when process is called, then ApiResponseDTO is set in Exchange message")
    void givenExchangeWithLoginRequestDTO_whenProcessCalled_thenApiResponseDTOSetInExchangeMessage() {
        //given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");
        AccessTokenResponse tokenResponse = new AccessTokenResponse();
        tokenResponse.setToken("<theBaerToken>");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/login");
        exchange.getIn().setBody(loginRequestDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Authenticated with name username successfully", tokenResponse);

        // mock
        when(authenticationApplicationService.getAccessToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())).thenReturn(tokenResponse);

        // when
        loginProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
