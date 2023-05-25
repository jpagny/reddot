package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.infrastructure.exception.type.HasNotRoleException;
import com.elysium.reddot.ms.message.infrastructure.exception.type.TokenNotActiveException;
import com.elysium.reddot.ms.message.infrastructure.inbound.constant.MessageRouteEnum;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.keycloak.KeycloakProcessorHolder;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.MessageProcessorHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import static org.apache.camel.support.builder.PredicateBuilder.not;

@Component
@RequiredArgsConstructor
public class MessageRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final MessageProcessorHolder messageProcessorHolder;
    private final KeycloakProcessorHolder keycloakProcessorHolder;
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
                .process(globalExceptionHandler);


        // definition routes
        rest().
                get().to(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName())
                .get(requestId).to(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName())
                .post().type(MessageDTO.class).to(MessageRouteEnum.CREATE_MESSAGE.getRouteName())
                .put(requestId).type(MessageDTO.class).to(MessageRouteEnum.UPDATE_MESSAGE.getRouteName());

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
                .log(LoggingLevel.ERROR, "Having role user is required")
                .process(exchange -> {
                    throw new HasNotRoleException("user");
                })
                .otherwise()
                .log("Token check complete. Processing now underway...")
                .end();

        // route : get all messages
        from(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName())
                .routeId("getAllMessages")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all messages")
                .process(messageProcessorHolder.getGetAllMessagesProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all messages")
                .end();

        // route : get message by id
        from(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName())
                .routeId("getMessageById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting message with id '${header.id}'")
                .process(messageProcessorHolder.getGetMessageByIdProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved message with id '${header.id}'")
                .end();

        // route : create message
        from(MessageRouteEnum.CREATE_MESSAGE.getRouteName())
                .routeId("createMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new message")
                .process(messageProcessorHolder.getCreateMessageProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created message")
                .end();

        // route : update message
        from(MessageRouteEnum.UPDATE_MESSAGE.getRouteName())
                .routeId("updateMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating message with id '${header.id}'")
                .process(messageProcessorHolder.getUpdateMessageProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated message with id '${header.id}'")
                .end();

    }
}
