package com.elysium.reddot.ms.thread.infrastructure.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeycloakTestUtils {

    public static final String TOKEN_URL = "http://localhost:11003/realms/reddot/protocol/openid-connect/token";
    public static final String CLIENT_ID = "reddot-app";
    public static final String CLIENT_SECRET = "H80mMKQZYyXf9S7yQ2cEAxRmXud0uCmU";

    @Data
    public static class UserToken {
        private String accessToken;
        private String userId;
    }

    public static UserToken obtainAccessToken(String username, String password) throws Exception {

        HttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(TOKEN_URL);
        String requestBody = "grant_type=password&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
                + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);
        StringEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setEntity(stringEntity);

        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String responseBody = EntityUtils.toString(entity);
            JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
            UserToken userToken = new UserToken();
            userToken.setAccessToken(jsonNode.get("access_token").asText());

            String userId = getUserIdFromToken(userToken.getAccessToken());
            userToken.setUserId(userId);

            return userToken;
        } else {
            throw new RuntimeException("No response body");

        }
    }

    public static KeycloakAuthenticationToken createAuthenticationToken(UserToken userToken, String role) {
        AccessToken accessToken = new AccessToken();
        accessToken.setSubject(userToken.getUserId());

        RefreshableKeycloakSecurityContext refreshableKeycloakSecurityContext = mock(RefreshableKeycloakSecurityContext.class);
        when(refreshableKeycloakSecurityContext.getToken()).thenReturn(accessToken);

        KeycloakSecurityContext securityContext = new KeycloakSecurityContext(null, accessToken, null, null);
        KeycloakPrincipal<KeycloakSecurityContext> principal = new KeycloakPrincipal<>("principal-" + userToken.getUserId(), securityContext);
        Set<String> realmRoles = new HashSet<>();
        realmRoles.add(role);

        return new KeycloakAuthenticationToken(new SimpleKeycloakAccount(principal, realmRoles, refreshableKeycloakSecurityContext), false);
    }

    private static String getUserIdFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("sub").asString();
    }

}
