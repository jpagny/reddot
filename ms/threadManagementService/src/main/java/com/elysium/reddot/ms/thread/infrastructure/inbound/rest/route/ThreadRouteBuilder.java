package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.infrastructure.constant.ThreadRouteEnum;
import com.elysium.reddot.ms.thread.infrastructure.exception.type.HasNotRoleException;
import com.elysium.reddot.ms.thread.infrastructure.exception.type.TokenNotActiveException;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.keycloak.KeycloakProcessorHolder;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread.ThreadProcessorHolder;
import lombok.AllArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import static org.apache.camel.support.builder.PredicateBuilder.not;

@Component
@AllArgsConstructor
public class ThreadRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final ThreadProcessorHolder threadProcessorHolder;
    private final KeycloakProcessorHolder keycloakProcessorHolder;

    @Override
    public void configure() {

        String requestId = "/{id}";

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        onException(Exception.class)
                .handled(true)
                .process(globalExceptionHandler);


        // definition routes
        rest().
                get().to(ThreadRouteEnum.GET_ALL_THREADS.getRouteName())
                .get(requestId).to(ThreadRouteEnum.GET_THREAD_BY_ID.getRouteName())
                .post().type(ThreadDTO.class).to(ThreadRouteEnum.CREATE_THREAD.getRouteName())
                .put(requestId).type(ThreadDTO.class).to(ThreadRouteEnum.UPDATE_THREAD.getRouteName())
                .delete(requestId).to(ThreadRouteEnum.DELETE_THREAD.getRouteName());

        // for all routes, intercept first check token
        interceptFrom()
                .log("Check token")
                .process(keycloakProcessorHolder.getCheckTokenProcessor())
                .choice()
                .when(header("active").isNotEqualTo(true))
                .log(LoggingLevel.ERROR, "The token is inactive")
                .process(exchange -> {
                    throw new TokenNotActiveException();
                })
                .when(not(header("roles").contains("user")))
                .log(LoggingLevel.ERROR, "Having role admin is required")
                .process(exchange -> {
                    throw new HasNotRoleException("user");
                })
                .otherwise()
                .log("Token check complete. Processing now underway...")
                .end();

        // route : get all threads
        from(ThreadRouteEnum.GET_ALL_THREADS.getRouteName())
                .routeId("getAllThreads")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all threads")
                .process(threadProcessorHolder.getGetAllThreadsProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all threads")
                .end();

        // route : get thread by id
        from(ThreadRouteEnum.GET_THREAD_BY_ID.getRouteName())
                .routeId("getThreadById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting thread with id '${header.id}'")
                .process(threadProcessorHolder.getGetThreadByIdProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved thread with id '${header.id}'")
                .end();

        // route : create thread
        from(ThreadRouteEnum.CREATE_THREAD.getRouteName())
                .routeId("createThread")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new thread")
                .process(threadProcessorHolder.getCreateThreadProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created thread with name '${body.data.name}'")
                .end();

        // route : update thread
        from(ThreadRouteEnum.UPDATE_THREAD.getRouteName())
                .routeId("updateThread")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating thread with id '${header.id}'")
                .process(threadProcessorHolder.getUpdateThreadProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated thread with id '${header.id}' and name '${body.data.name}'")
                .end();

        // route : delete thread
        from(ThreadRouteEnum.DELETE_THREAD.getRouteName())
                .routeId("deleteThread")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Deleting thread with id '${header.id}'")
                .process(threadProcessorHolder.getDeleteThreadProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully deleted thread with id '${header.id}'")
                .end();

    }
}
