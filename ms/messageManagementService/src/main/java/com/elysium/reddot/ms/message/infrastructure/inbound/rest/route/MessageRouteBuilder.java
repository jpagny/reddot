package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.MessageProcessorHolder;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageRouteBuilder extends RouteBuilder {

    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;
    private final MessageProcessorHolder messageProcessorHolder;

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
                get().to(MessageRouteConstants.GET_ALL_TOPICS.getRouteName())
                .get(requestId).to(MessageRouteConstants.GET_TOPIC_BY_ID.getRouteName())
                .post().type(MessageDTO.class).to(MessageRouteConstants.CREATE_TOPIC.getRouteName())
                .put(requestId).type(MessageDTO.class).to(MessageRouteConstants.UPDATE_TOPIC.getRouteName());

        // route : get all messages
        from(MessageRouteConstants.GET_ALL_TOPICS.getRouteName())
                .routeId("getAllMessages")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all messages")
                .process(messageProcessorHolder.getGetAllMessagesProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all messages")
                .end();

        // route : get message by id
        from(MessageRouteConstants.GET_TOPIC_BY_ID.getRouteName())
                .routeId("getMessageById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting message with id '${header.id}'")
                .process(messageProcessorHolder.getGetMessageByIdProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved message with id '${header.id}'")
                .end();

        // route : create message
        from(MessageRouteConstants.CREATE_TOPIC.getRouteName())
                .routeId("createMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new message")
                .process(messageProcessorHolder.getCreateMessageProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created message with name '${body.data.name}'")
                .end();

        // route : update message
        from(MessageRouteConstants.UPDATE_TOPIC.getRouteName())
                .routeId("updateMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating message with id '${header.id}'")
                .process(messageProcessorHolder.getUpdateMessageProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated message with id '${header.id}' and name '${body.data.name}'")
                .end();

    }
}
