package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.infrastructure.exception.type.CircuitBreakerException;
import com.elysium.reddot.ms.user.infrastructure.constant.UserRouteEnum;
import com.elysium.reddot.ms.user.infrastructure.exception.processor.CircuitBreakerExceptionHandler;
import com.elysium.reddot.ms.user.infrastructure.exception.processor.GlobalExceptionHandler;
import com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.UserProcessorHolder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

/**
 * Configure the routes for user management.
 */
@Component
@RequiredArgsConstructor
public class UserRouteBuilder extends RouteBuilder {

    private final UserProcessorHolder userProcessorHolder;

    @Override
    public void configure() {

        // rest configuration
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        // global exception handling
        onException(Exception.class)
                .handled(true)
                .process(new GlobalExceptionHandler());

        // circuit breaker exception handling
        onException(CircuitBreakerException.class)
                .handled(true)
                .process(new CircuitBreakerExceptionHandler());

        // definition route
        rest().
                post("/register").type(UserDTO.class).to(UserRouteEnum.USER_REGISTRATION.getRouteName());

        // route : register a new user
        from(UserRouteEnum.USER_REGISTRATION.getRouteName())
                .routeId("userRegistration")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Registering a new user")
                .circuitBreaker()
                .process(userProcessorHolder.getCreateUserProcessor())
                .onFallback()
                .process(exchange -> {
                    Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    throw new CircuitBreakerException("An internal server error occurred.", cause);
                })
                .log("A fallback operation for '${routeId}'")
                .end()
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully registered user '${body.data.name}'")
                .end();
    }
}
