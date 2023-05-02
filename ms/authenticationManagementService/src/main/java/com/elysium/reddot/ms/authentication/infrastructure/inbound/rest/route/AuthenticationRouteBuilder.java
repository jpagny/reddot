package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.authentication.application.data.dto.LoginRequestDTO;
import com.elysium.reddot.ms.authentication.application.data.dto.UserAuthenticatedDTO;
import com.elysium.reddot.ms.authentication.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor.AuthenticationProcessorHolder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthenticationRouteBuilder extends RouteBuilder {

    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;
    private final AuthenticationProcessorHolder authenticationProcessorHolder;

    @Override
    public void configure() {

        onException(Exception.class)
                .handled(true)
                .process(camelGlobalExceptionHandler);

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest()
                .post("/login").type(LoginRequestDTO.class).to(AuthenticationRouteConstants.LOGIN.getRouteName())
                .post("/logout").type(UserAuthenticatedDTO.class).to(AuthenticationRouteConstants.LOGOUT.getRouteName());

        from(AuthenticationRouteConstants.LOGIN.getRouteName())
                .routeId("login")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': try to authenticate")
                .process(authenticationProcessorHolder.getLoginProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully authenticated")
                .end();

        from(AuthenticationRouteConstants.LOGOUT.getRouteName())
                .routeId("logout")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': try to logout")
                .process(authenticationProcessorHolder.getLogoutProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully logged out")
                .end();

    }
}
