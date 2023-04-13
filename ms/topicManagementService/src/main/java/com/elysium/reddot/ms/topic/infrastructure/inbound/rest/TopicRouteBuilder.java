package com.elysium.reddot.ms.topic.infrastructure.inbound.rest;


import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopicRouteBuilder extends RouteBuilder {

    private final TopicApplicationServiceImpl topicService;
    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;

    public TopicRouteBuilder(TopicApplicationServiceImpl topicService, CamelGlobalExceptionHandler camelGlobalExceptionHandler) {
        this.topicService = topicService;
        this.camelGlobalExceptionHandler = camelGlobalExceptionHandler;
    }

    @Override
    public void configure() {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        onException(Exception.class)
                .handled(true)
                .process(camelGlobalExceptionHandler);


        rest("topics")
                .produces("application/json")
                .consumes("application/json")

                .get().routeId("processGetAllTopics").to("direct:getAllTopics")
                .get("/{id}").routeId("processGetTopicById").to("direct:getTopicById")
                .post().type(TopicDTO.class).routeId("processCreateTopic").to("direct:createTopic")
                .put("/{id}").type(TopicDTO.class).routeId("processUpdateTopic").to("direct:updateTopic")
                .delete("/{id}").routeId("processDeleteTopic").to("direct:deleteTopic");

        from("direct:getAllTopics")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    List<TopicDTO> topics = topicService.getAllTopics();

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "All topics retrieved successfully", topics);

                    exchange.getMessage().setBody(apiResponseDTO);
                });

        from("direct:getTopicById")
                .process(exchange -> {
                    Long id = Long.parseLong(exchange.getIn().getHeader("id").toString());
                    TopicDTO topic = topicService.getTopicById(id);

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with id " + id + " retrieved successfully", topic);

                    exchange.getMessage().setBody(apiResponseDTO);
                });

        from("direct:createTopic")
                .process(exchange -> {
                    TopicDTO topicDTO = exchange.getIn().getBody(TopicDTO.class);
                    TopicDTO topicCreated = topicService.createTopic(topicDTO);

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(), "Topic with name " + topicCreated.getName() + " created successfully", topicCreated);

                    exchange.getMessage().setBody(apiResponseDTO);
                });

        from("direct:updateTopic")
                .process(exchange -> {
                    Long id = exchange.getIn().getHeader("id", Long.class);
                    TopicDTO topicDto = exchange.getIn().getBody(TopicDTO.class);

                    TopicDTO updatedTopic = topicService.updateTopic(id, topicDto);

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with name " + updatedTopic.getName() + " updated successfully", updatedTopic);
                    exchange.getMessage().setBody(apiResponseDTO);
                });

        from("direct:deleteTopic")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    Long id = exchange.getIn().getHeader("id", Long.class);
                    TopicDTO topicDTO = topicService.getTopicById(id);

                    topicService.deleteTopicById(id);

                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with id " + id + " deleted successfully",topicDTO);
                    exchange.getMessage().setBody(apiResponseDTO);
                });


    }
}
