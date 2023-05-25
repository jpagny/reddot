package com.elysium.reddot.ms.topic.application.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@Slf4j
public class KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.resource}")
    private String resource;

    @Value("${keycloak.credentials.secret}")
    private String credentialsSecret;

    public String callTokenIntrospectionEndpoint(String authHeader) throws URISyntaxException, IOException {
        String tokenIntrospectUrl = getTokenIntrospectUrl();
        URIBuilder uriBuilder = new URIBuilder(tokenIntrospectUrl);

        String requestBody = "client_id=" + resource + "&client_secret=" + credentialsSecret + "&token=" + authHeader;
        StringEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED);
        HttpPost httpPost = new HttpPost(uriBuilder.build());

        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity == null) {
                throw new IOException("No response body");
            }

            return EntityUtils.toString(entity);
        }
    }


    private String getTokenIntrospectUrl() {
        return authServerUrl + "realms/" + realm + "/protocol/openid-connect/token/introspect";
    }

}
