package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.infrastructure.constant.TopicRouteEnum;
import com.elysium.reddot.ms.topic.infrastructure.exception.type.HasNotRoleException;
import com.elysium.reddot.ms.topic.infrastructure.exception.type.TokenNotActiveException;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.keycloak.KeycloakProcessorHolder;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.topic.TopicProcessorHolder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import static org.apache.camel.support.builder.PredicateBuilder.not;

@Component
@RequiredArgsConstructor
public class TopicRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final TopicProcessorHolder topicProcessorHolder;
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
                get().to(TopicRouteEnum.GET_ALL_TOPICS.getRouteName())
                .get(requestId).to(TopicRouteEnum.GET_TOPIC_BY_ID.getRouteName())
                .post().type(TopicDTO.class).to(TopicRouteEnum.CREATE_TOPIC.getRouteName())
                .put(requestId).type(TopicDTO.class).to(TopicRouteEnum.UPDATE_TOPIC.getRouteName())
                .delete(requestId).to(TopicRouteEnum.DELETE_TOPIC.getRouteName());

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


        // route : get all topics
        from(TopicRouteEnum.GET_ALL_TOPICS.getRouteName())
                .routeId("getAllTopics")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all topics")
                .process(topicProcessorHolder.getGetAllTopicsProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all topics")
                .end();

        // route : get topic by id
        from(TopicRouteEnum.GET_TOPIC_BY_ID.getRouteName())
                .routeId("getTopicById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting topic with id '${header.id}'")
                .process(topicProcessorHolder.getGetTopicByIdProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved topic with id '${header.id}'")
                .end();

        // route : create topic
        from(TopicRouteEnum.CREATE_TOPIC.getRouteName())
                .routeId("createTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new topic")
                .process(topicProcessorHolder.getCreateTopicProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created topic with name '${body.data.name}'")
                .end();

        // route : update topic
        from(TopicRouteEnum.UPDATE_TOPIC.getRouteName())
                .routeId("updateTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating topic with id '${header.id}'")
                .process(topicProcessorHolder.getUpdateTopicProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated topic with id '${header.id}' and name '${body.data.name}'")
                .end();

        // route : delete topic
        from(TopicRouteEnum.DELETE_TOPIC.getRouteName())
                .routeId("deleteTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Deleting topic with id '${header.id}'")
                .process(topicProcessorHolder.getDeleteTopicProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully deleted topic with id '${header.id}'")
                .end();

    }
}
