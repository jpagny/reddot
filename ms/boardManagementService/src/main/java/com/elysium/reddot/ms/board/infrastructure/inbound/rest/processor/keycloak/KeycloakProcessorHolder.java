package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.keycloak;

import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class KeycloakProcessorHolder {
    private final CheckTokenProcessor checkTokenProcessor;
}