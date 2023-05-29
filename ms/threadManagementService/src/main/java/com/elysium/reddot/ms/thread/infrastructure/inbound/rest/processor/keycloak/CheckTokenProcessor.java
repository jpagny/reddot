package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.keycloak;

import com.elysium.reddot.ms.thread.application.service.KeycloakService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class CheckTokenProcessor implements Processor {

    private final KeycloakService keycloakService;

    @Override
    public void process(Exchange exchange) throws Exception {

    }

}
