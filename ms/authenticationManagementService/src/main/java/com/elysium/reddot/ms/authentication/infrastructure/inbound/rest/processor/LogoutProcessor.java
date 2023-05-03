package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.service.AuthenticationApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component()
@Slf4j
public class LogoutProcessor implements Processor {

    @Autowired
    AuthenticationApplicationService authenticationApplicationService;


    @Override
    public void process(Exchange exchange) {

        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
        authenticationApplicationService.logout(request);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "You have been logged out successfully.", null);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.FOUND.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
