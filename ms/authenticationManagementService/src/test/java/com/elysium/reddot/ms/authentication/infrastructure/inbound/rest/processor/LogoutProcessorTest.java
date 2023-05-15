package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.UserAuthenticatedDTO;
import com.elysium.reddot.ms.authentication.application.service.AuthenticationApplicationService;
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
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class LogoutProcessorTest {

    private LogoutProcessor logoutProcessor;

    @Mock
    private AuthenticationApplicationService authenticationApplicationService;

    private CamelContext camelContext;

    @BeforeEach
    void setup() {
        camelContext = new DefaultCamelContext();
        logoutProcessor = new LogoutProcessor(authenticationApplicationService);
    }




}
