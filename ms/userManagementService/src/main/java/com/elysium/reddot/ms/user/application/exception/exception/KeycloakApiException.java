package com.elysium.reddot.ms.user.application.exception.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import javax.ws.rs.core.Response;

@Getter
public class KeycloakApiException extends RuntimeException {

    private final int status;
    private final Response response;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public KeycloakApiException(Response response) {
        super(String.format("Error Keycloak API : %s",
                getResponseBody(response)));
        this.status = response.getStatus();
        this.response = response;
    }

    private static String getResponseBody(Response response) {

        if (response.hasEntity()) {
            String responseBody = response.readEntity(String.class);

            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                if (jsonNode.has("errorMessage")) {
                    return jsonNode.get("errorMessage").asText();

                } else {
                    return "Error message not found in response body";

                }

            } catch (JsonProcessingException e) {
                return String.format("Failed to parse JSON response: %s", e.getMessage());

            } catch (Exception e) {
                return String.format("Unexpected exception: %s", e.getMessage());

            }
        }

        return "No response body available";
    }


}