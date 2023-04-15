package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.TopicProcessorHolder;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TopicRouteBuilder extends RouteBuilder {

    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;
    private final TopicProcessorHolder topicProcessorHolder;

    @Override
    public void configure() {

        String requestId = "/{id}";

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "My API Title")
                .apiProperty("api.version", "1.0.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "/api");

        onException(Exception.class)
                .handled(true)
                .process(camelGlobalExceptionHandler);


        // definition routes
        rest().
                get().to(TopicRouteConstants.GET_ALL_TOPICS.getRouteName())
                .get(requestId).to(TopicRouteConstants.GET_TOPIC_BY_ID.getRouteName())
                .post().type(TopicDTO.class).to(TopicRouteConstants.CREATE_TOPIC.getRouteName())
                .put(requestId).type(TopicDTO.class).to(TopicRouteConstants.UPDATE_TOPIC.getRouteName())
                .delete(requestId).to(TopicRouteConstants.DELETE_TOPIC.getRouteName());

        // route : get all topics
        from(TopicRouteConstants.GET_ALL_TOPICS.getRouteName())
                .routeId("getAllTopics")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all topics")
                .process(topicProcessorHolder.getGetAllTopicsProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all topics")
                .end();

        // route : get topic by id
        from(TopicRouteConstants.GET_TOPIC_BY_ID.getRouteName())
                .routeId("getTopicById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting topic with id '${header.id}'")
                .process(topicProcessorHolder.getGetTopicByIdProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved topic with id '${header.id}'")
                .end();

        // route : create topic
        from(TopicRouteConstants.CREATE_TOPIC.getRouteName())
                .routeId("createTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new topic")
                .process(topicProcessorHolder.getCreateTopicProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created topic with name '${body.data.name}'")
                .end();

        // route : update topic
        from(TopicRouteConstants.UPDATE_TOPIC.getRouteName())
                .routeId("updateTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating topic with id '${header.id}'")
                .process(topicProcessorHolder.getUpdateTopicProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated topic with id '${header.id}' and name '${body.data.name}'")
                .end();

        // route : delete topic
        from(TopicRouteConstants.DELETE_TOPIC.getRouteName())
                .routeId("deleteTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Deleting topic with id '${header.id}'")
                .process(topicProcessorHolder.getDeleteTopicProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully deleted topic with id '${header.id}'")
                .end();

    }
}
