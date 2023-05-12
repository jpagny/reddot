package com.elysium.reddot.ms.user.infrastructure.exception.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * Exception thrown when an error occurs while interacting with the Keycloak API.
 * This exception provides information about the status code and response from the API.
 */
@Getter
public class KeycloakApiException extends RuntimeException {

    private final int status;
    private final Response response;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a KeycloakApiException with the specified response.
     *
     * @param response the response from the Keycloak API
     */
    public KeycloakApiException(Response response) {
        super(String.format("Error Keycloak API : %s",
                getResponseBody(response)));
        this.status = response.getStatus();
        this.response = response;
    }

    /**
     * Retrieves the error message from the response body, if available.
     *
     * @param response the response from the Keycloak API
     * @return the error message extracted from the response body, or a default message if not found
     */
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