package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.LoginRequestDTO;
import com.elysium.reddot.ms.authentication.application.service.AuthenticationApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginProcessor implements Processor {

    private final AuthenticationApplicationService authenticationApplicationService;

    /**
     * Processes the input login request to authenticate a user.
     *
     * <p>This method retrieves the LoginRequestDTO from the Exchange's input body, which contains
     * the user's username and password. It then requests an access token via the
     * authenticationApplicationService. If successful, it creates an ApiResponseDTO with the
     * authentication status and token and sets it as the body of the Exchange's message.
     * It also sets the HTTP response code to CREATED (201).
     *
     * @param exchange the Exchange containing the input LoginRequestDTO and the output ApiResponseDTO
     */
    @Override
    public void process(Exchange exchange) {
        LoginRequestDTO inputLoginRequest = exchange.getIn().getBody(LoginRequestDTO.class);
        log.debug("Received input UserDTO: {}", inputLoginRequest);

        AccessTokenResponse token = authenticationApplicationService.getAccessToken(inputLoginRequest.getUsername(), inputLoginRequest.getPassword());
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Authenticated with name " + inputLoginRequest.getUsername() + " successfully", token);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
