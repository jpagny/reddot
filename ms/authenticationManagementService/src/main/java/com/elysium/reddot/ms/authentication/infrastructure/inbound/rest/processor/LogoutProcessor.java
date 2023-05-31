package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.service.AuthenticationApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutProcessor implements Processor {

    private final AuthenticationApplicationService authenticationApplicationService;

    /**
     * Processes the request to log out a user.
     *
     * <p>This method retrieves the HttpServletRequest from the Exchange's input body, which is used
     * to log out the user via the authenticationApplicationService. It then creates an ApiResponseDTO
     * with the logout status and sets it as the body of the Exchange's message.
     * It also sets the HTTP response code to FOUND (302).
     *
     * @param exchange the Exchange containing the input HttpServletRequest and the output ApiResponseDTO
     */
    @Override
    public void process(Exchange exchange) {
        String inputToken = exchange.getIn().getHeader("Authorization", String.class);
        log.debug("Received input token: {}", inputToken);

        ResponseEntity<String> response = authenticationApplicationService.logout(inputToken);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "You have been logged out successfully.", response);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.FOUND.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
