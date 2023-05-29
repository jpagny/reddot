package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.infrastructure.constant.MessageRouteEnum;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.MessageProcessorHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final MessageProcessorHolder messageProcessorHolder;
    private final ObjectMapper objectMapper;

    @Override
    public void configure() {

        JacksonDataFormat format = new JacksonDataFormat();
        format.setObjectMapper(objectMapper);

        String requestId = "/{id}";

        restConfiguration()
                .component("servlet")
                .dataFormatProperty("prettyPrint", "true");

        onException(Exception.class)
                .handled(true)
                .process(globalExceptionHandler)
                .marshal(format);

        // definition routes
        rest().
                get().to(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName())
                .get(requestId).to(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName())
                .post().type(MessageDTO.class).to(MessageRouteEnum.CREATE_MESSAGE.getRouteName())
                .put(requestId).type(MessageDTO.class).to(MessageRouteEnum.UPDATE_MESSAGE.getRouteName());

        // route : get all messages
        from(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName())
                .routeId("getAllMessages")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all messages")
                .policy("userPolicy")
                .process(messageProcessorHolder.getGetAllMessagesProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all messages")
                .end();

        // route : get message by id
        from(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName())
                .routeId("getMessageById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting message with id '${header.id}'")
                .policy("userPolicy")
                .process(messageProcessorHolder.getGetMessageByIdProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved message with id '${header.id}'")
                .end();

        // route : create message
        from(MessageRouteEnum.CREATE_MESSAGE.getRouteName())
                .routeId("createMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new message")
                .policy("userPolicy")
                .process(messageProcessorHolder.getCreateMessageProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created message")
                .end();

        // route : update message
        from(MessageRouteEnum.UPDATE_MESSAGE.getRouteName())
                .routeId("updateMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating message with id '${header.id}'")
                .policy("userPolicy")
                .process(messageProcessorHolder.getUpdateMessageProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated message with id '${header.id}'")
                .end();

    }
}
