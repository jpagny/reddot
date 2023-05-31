package com.elysium.reddot.ms.authentication.infrastructure.filter;

import com.elysium.reddot.ms.authentication.infrastructure.data.exception.GlobalExceptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomKeycloakAuthenticationProcessingFilter extends KeycloakAuthenticationProcessingFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CustomKeycloakAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String wwwAuthenticate = response.getHeader("WWW-Authenticate");
        String infoError = extractInformationError(wwwAuthenticate);

        GlobalExceptionDTO globalExceptionDTO = new GlobalExceptionDTO(authException.getClass().getSimpleName(),
                authException.getMessage() + " -> " + infoError);

        String json = objectMapper.writeValueAsString(globalExceptionDTO);

        try (PrintWriter out = response.getWriter()) {
            out.print(json);
        }
    }

    private String extractInformationError(String wwwAuthenticate) {
        Pattern pattern = Pattern.compile("error=\"(.*?)\", error_description=\"(.*?)\"");
        Matcher matcher = pattern.matcher(wwwAuthenticate);
        String result = "No content";

        if (matcher.find()) {
            String error = matcher.group(1);
            String errorDescription = matcher.group(2);
            result = error + " : " + errorDescription;
        }

        return result;
    }


}
