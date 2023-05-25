package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.keycloak;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class KeycloakProcessorHolder {
    private final CheckTokenProcessor checkTokenProcessor;
}