package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.infrastructure.constant.ReplyMessageRouteEnum;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage.ReplyMessageProcessorHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReplyMessageRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final ReplyMessageProcessorHolder replyMessageProcessorHolder;
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
                get().to(ReplyMessageRouteEnum.GET_ALL_REPLIES_MESSAGE.getRouteName())
                .get(requestId).to(ReplyMessageRouteEnum.GET_REPLY_MESSAGE_BY_ID.getRouteName())
                .post().type(ReplyMessageDTO.class).to(ReplyMessageRouteEnum.CREATE_REPLY_MESSAGE.getRouteName())
                .put(requestId).type(ReplyMessageDTO.class).to(ReplyMessageRouteEnum.UPDATE_REPLY_MESSAGE.getRouteName());

        // route : get all replyMessages
        from(ReplyMessageRouteEnum.GET_ALL_REPLIES_MESSAGE.getRouteName())
                .routeId("getAllReplyMessages")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all replyMessages")
                .process(replyMessageProcessorHolder.getGetAllRepliesMessageProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all replyMessages")
                .end();

        // route : get replyMessage by id
        from(ReplyMessageRouteEnum.GET_REPLY_MESSAGE_BY_ID.getRouteName())
                .routeId("getReplyMessageById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting replyMessage with id '${header.id}'")
                .process(replyMessageProcessorHolder.getGetReplyMessageByIdProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved replyMessage with id '${header.id}'")
                .end();

        // route : create replyMessage
        from(ReplyMessageRouteEnum.CREATE_REPLY_MESSAGE.getRouteName())
                .routeId("createReplyMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new replyMessage")
                .process(replyMessageProcessorHolder.getCreateReplyMessageProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created replyMessage")
                .end();

        // route : update replyMessage
        from(ReplyMessageRouteEnum.UPDATE_REPLY_MESSAGE.getRouteName())
                .routeId("updateReplyMessage")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating replyMessage with id '${header.id}'")
                .process(replyMessageProcessorHolder.getUpdateReplyMessageProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated replyMessage with id '${header.id}'")
                .end();

    }
}
