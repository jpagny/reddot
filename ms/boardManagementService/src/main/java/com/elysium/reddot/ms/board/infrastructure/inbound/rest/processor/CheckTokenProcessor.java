package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.infrastructure.data.exception.TokenNotValidException;
import com.elysium.reddot.ms.board.infrastructure.data.property.KeycloakProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class CheckTokenProcessor implements Processor {

    private final KeycloakProperties keycloakProperties;
    private String tokenIntrospectUrl;

    public String getTokenIntrospectUrl() {
        return keycloakProperties.getAuthServerUrl() + "realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token/introspect";
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.debug("Processing check Token");

        String authHeader = extractAuthorizationHeader(exchange);

        validateToken(authHeader);

        authHeader = authHeader.substring("Bearer ".length());

        String responseBody = callTokenIntrospectionEndpoint(authHeader);

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

    private String callTokenIntrospectionEndpoint(String authHeader) throws IOException, URISyntaxException {
        HttpClient httpClient = HttpClients.createDefault();
        tokenIntrospectUrl = getTokenIntrospectUrl();
        URIBuilder uriBuilder = new URIBuilder(tokenIntrospectUrl);
        String requestBody = "client_id=" + keycloakProperties.getResource() + "&client_secret=" + keycloakProperties.getCredentialsSecret() + "&token=" + authHeader;

        StringEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        if (entity == null) {
            throw new RuntimeException("No response body");
        }

        return EntityUtils.toString(entity);
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
