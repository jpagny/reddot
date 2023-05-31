package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.infrastructure.constant.UserRouteEnum;
import com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.user.UserProcessorHolder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

/**
 * Configure the routes for user management.
 */
@Component
@RequiredArgsConstructor
public class UserRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final UserProcessorHolder userProcessorHolder;

    @Override
    public void configure() {

        // rest configuration
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true");

        // global exception handling
        onException(Exception.class)
                .handled(true)
                .process(globalExceptionHandler)
                .end();

        // definition route
        rest().
                post("/register").type(UserDTO.class).to(UserRouteEnum.USER_REGISTRATION.getRouteName());

        // route : register a new user
        from(UserRouteEnum.USER_REGISTRATION.getRouteName())
                .routeId("userRegistration")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Registering a new user")
                .process(userProcessorHolder.getCreateUserProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully registered user '${body.data.username}'")
                .end();
    }
}
