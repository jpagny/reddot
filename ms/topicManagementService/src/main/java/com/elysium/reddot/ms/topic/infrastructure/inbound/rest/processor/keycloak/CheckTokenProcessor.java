package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.keycloak;

import com.elysium.reddot.ms.topic.application.service.KeycloakService;
import com.elysium.reddot.ms.topic.infrastructure.exception.type.TokenNotValidException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class CheckTokenProcessor implements Processor {

    private final KeycloakService keycloakService;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.debug("Processing check Token");

        String authHeader = extractAuthorizationHeader(exchange);

        log.debug("Checking token validity");
        validateToken(authHeader);
        log.debug("Token is valid");

        authHeader = authHeader.substring("Bearer ".length());

        String responseBody = keycloakService.callTokenIntrospectionEndpoint(authHeader);

        List<String> roles = extractRolesFromResponse(responseBody);
        String isActive = extractIsActiveFromResponse(responseBody);

        exchange.getIn().setHeader("active", isActive);
        exchange.getIn().setHeader("roles", String.join(",", roles));

        log.info("Token validation completed successfully. Active: {}, Roles: {}", isActive, roles);
    }

    private String extractAuthorizationHeader(Exchange exchange) {
        return exchange.getIn().getHeader("Authorization", String.class);
    }

    private void validateToken(String authHeader) {
        if (authHeader == null || !authHeader.toLowerCase().startsWith("bearer ")) {
            throw new TokenNotValidException();
        }
    }

    private List<String> extractRolesFromResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseBody);
        JsonNode realmAccessNode = rootNode.get("realm_access");
        JsonNode rolesNode = realmAccessNode.get("roles");

        List<String> roles = new ArrayList<>();
        if (rolesNode != null) {
            for (JsonNode role : rolesNode) {
                roles.add(role.asText());
            }
        }

        return roles;
    }

    private String extractIsActiveFromResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseBody);

        return rootNode.get("active").toString();
    }
}
