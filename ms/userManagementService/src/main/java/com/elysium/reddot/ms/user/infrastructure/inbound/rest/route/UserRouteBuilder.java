package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.UserProcessorHolder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRouteBuilder extends RouteBuilder {

    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;
    private final UserProcessorHolder userProcessorHolder;


    @Override
    public void configure() {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        onException(Exception.class)
                .handled(true)
                .process(camelGlobalExceptionHandler);

        rest().
                post().type(UserDTO.class).to(UserRouteConstants.CREATE_TOPIC.getRouteName());

        from(UserRouteConstants.CREATE_TOPIC.getRouteName())
                .routeId("createUser")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new user")
                .process(userProcessorHolder.getCreateUserProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created topic with name '${body.data.name}'")
                .end();
    }
}
