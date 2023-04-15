package com.elysium.reddot.ms.topic.infrastructure.inbound.rest;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.infrastructure.inbound.camel.GetAllTopicsProcessor;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TopicRouteBuilder extends RouteBuilder {

    private final GetAllTopicsProcessor getAllTopicsProcessor;
    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;


    @Override
    public void configure() {

        String mediaType = "application/json";
        String requestId = "/{id}";

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        onException(Exception.class)
                .handled(true)
                .process(camelGlobalExceptionHandler);

        rest("topics")
                .produces(mediaType)
                .consumes(mediaType)

                .get().to("direct:getAllTopics")
                .get(requestId).to("direct:getTopicById")
                .post().type(TopicDTO.class).to("direct:createTopic")
                .put(requestId).type(TopicDTO.class).to("direct:updateTopic")
                .delete(requestId).to("direct:deleteTopic");

        // Get all topics
        from("direct:getAllTopics")
                .routeId("getAllTopics")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all topics")
                .process(getAllTopicsProcessor)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all topics");


        /*
        from("direct:getAllTopics")
                .routeId("getAllTopics")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all topics")
                .process(exchange -> {
                    List<TopicDTO> topics = topicService.getAllTopics();
                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                            "All topics retrieved successfully", topics);
                    exchange.getMessage().setBody(apiResponseDTO);
                })
                .log("Route '${routeId}': Path 'Path '${header.CamelHttpUri}': Successfully retrieved all topics");

        // Get topic by id
        from("direct:getTopicById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting topic with id '${header.id}'")
                .process(exchange -> {
                    Long id = Long.parseLong(exchange.getIn().getHeader("id").toString());
                    TopicDTO topic = topicService.getTopicById(id);

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                            "Topic with id " + id + " retrieved successfully", topic);
                    exchange.getMessage().setBody(apiResponseDTO);
                })
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved topic with id '${header.id}'");

        // Create topic
        from("direct:createTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new topic")
                .process(exchange -> {
                    TopicDTO topicDTO = exchange.getIn().getBody(TopicDTO.class);
                    TopicDTO topicCreated = topicService.createTopic(topicDTO);
                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                            "Topic with name " + topicCreated.getName() + " created successfully", topicCreated);
                    exchange.getMessage().setBody(apiResponseDTO);
                })
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created topic with name '${body.data.name}'");

        // Update topic
        from("direct:updateTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating topic with id '${header.id}'")
                .process(exchange -> {
                    Long id = exchange.getIn().getHeader("id", Long.class);
                    TopicDTO topicDto = exchange.getIn().getBody(TopicDTO.class);

                    TopicDTO updatedTopic = topicService.updateTopic(id, topicDto);

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with name " + updatedTopic.getName() + " updated successfully", updatedTopic);
                    exchange.getMessage().setBody(apiResponseDTO);
                })
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated topic with id '${header.id}' and name '${body.data.name}'");

        // Delete topic
        from("direct:deleteTopic")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Deleting topic with id '${header.id}'")
                .process(exchange -> {
                    Long id = exchange.getIn().getHeader("id", Long.class);
                    TopicDTO topicDTO = topicService.getTopicById(id);

                    topicService.deleteTopicById(id);

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with id " + id + " deleted successfully", topicDTO);
                    exchange.getMessage().setBody(apiResponseDTO);
                })
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully deleted topic with id '${header.id}'");


         */

    }
}
