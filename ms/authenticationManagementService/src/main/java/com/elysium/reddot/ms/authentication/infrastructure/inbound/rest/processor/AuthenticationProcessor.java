package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.LoginRequestDTO;
import com.elysium.reddot.ms.authentication.application.service.AuthenticationApplicationService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.keycloak.Token;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProcessor implements Processor {

    @Autowired
    AuthenticationApplicationService authenticationApplicationService;

    @Override
    public void process(Exchange exchange) {
        LoginRequestDTO inputLoginRequest = exchange.getIn().getBody(LoginRequestDTO.class);

        AccessTokenResponse token = authenticationApplicationService.getAccessToken(inputLoginRequest.getUsername(),inputLoginRequest.getPassword());
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Authenticated with name " + inputLoginRequest.getUsername() + " successfully", token);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
