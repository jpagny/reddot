package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.ThreadProcessorHolder;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ThreadRouteBuilder extends RouteBuilder {

    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;
    private final ThreadProcessorHolder threadProcessorHolder;

    @Override
    public void configure() {

        String requestId = "/{id}";

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        onException(Exception.class)
                .handled(true)
                .process(camelGlobalExceptionHandler);


        // definition routes
        rest().
                get().to(ThreadRouteConstants.GET_ALL_TOPICS.getRouteName())
                .get(requestId).to(ThreadRouteConstants.GET_TOPIC_BY_ID.getRouteName())
                .post().type(ThreadDTO.class).to(ThreadRouteConstants.CREATE_TOPIC.getRouteName())
                .put(requestId).type(ThreadDTO.class).to(ThreadRouteConstants.UPDATE_TOPIC.getRouteName())
                .delete(requestId).to(ThreadRouteConstants.DELETE_TOPIC.getRouteName());

        // route : get all threads
        from(ThreadRouteConstants.GET_ALL_TOPICS.getRouteName())
                .routeId("getAllThreads")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all threads")
                .process(threadProcessorHolder.getGetAllThreadsProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all threads")
                .end();

        // route : get thread by id
        from(ThreadRouteConstants.GET_TOPIC_BY_ID.getRouteName())
                .routeId("getThreadById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting thread with id '${header.id}'")
                .process(threadProcessorHolder.getGetThreadByIdProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved thread with id '${header.id}'")
                .end();

        // route : create thread
        from(ThreadRouteConstants.CREATE_TOPIC.getRouteName())
                .routeId("createThread")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new thread")
                .process(threadProcessorHolder.getCreateThreadProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created thread with name '${body.data.name}'")
                .end();

        // route : update thread
        from(ThreadRouteConstants.UPDATE_TOPIC.getRouteName())
                .routeId("updateThread")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating thread with id '${header.id}'")
                .process(threadProcessorHolder.getUpdateThreadProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated thread with id '${header.id}' and name '${body.data.name}'")
                .end();

        // route : delete thread
        from(ThreadRouteConstants.DELETE_TOPIC.getRouteName())
                .routeId("deleteThread")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Deleting thread with id '${header.id}'")
                .process(threadProcessorHolder.getDeleteThreadProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully deleted thread with id '${header.id}'")
                .end();

    }
}
