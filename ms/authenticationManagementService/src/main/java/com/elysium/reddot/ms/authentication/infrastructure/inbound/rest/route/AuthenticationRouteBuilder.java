package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.authentication.application.data.dto.LoginRequestDTO;
import com.elysium.reddot.ms.authentication.infrastructure.exception.processor.GlobalExceptionHandler;
import com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor.AuthenticationProcessorHolder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.keycloak.adapters.springsecurity.KeycloakAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthenticationRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final AuthenticationProcessorHolder authenticationProcessorHolder;

    @Override
    public void configure() {

        onException(Exception.class)
                .handled(true)
                .process(globalExceptionHandler);

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest()
                .post("/login").type(LoginRequestDTO.class).to(AuthenticationRouteEnum.LOGIN.getRouteName())
                .post("/logout").to(AuthenticationRouteEnum.LOGOUT.getRouteName());

        from(AuthenticationRouteEnum.LOGIN.getRouteName())
                .routeId("login")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': try to authenticate")
                .process(authenticationProcessorHolder.getLoginProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully authenticated")
                .end();

        from(AuthenticationRouteEnum.LOGOUT.getRouteName())
                .routeId("logout")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': try to logout")
                .process(authenticationProcessorHolder.getLogoutProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully logged out")
                .end();

        from("direct:handleAuthFailure")
                .process(exchange -> {
                    System.out.println("KeycloakAuthenticationException " + exchange.getIn().getBody().toString());
                    throw new Exception(exchange.getIn().getBody(KeycloakAuthenticationException.class));
                });

    }
}
