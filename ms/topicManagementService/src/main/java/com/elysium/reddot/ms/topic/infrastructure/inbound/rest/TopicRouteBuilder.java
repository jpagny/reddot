package com.elysium.reddot.ms.topic.infrastructure.inbound.rest;


import com.elysium.reddot.ms.topic.application.exception.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.application.service.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
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
    private CamelGlobalExceptionHandler camelGlobalExceptionHandler;

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

                .get().routeId("getAllTopics").to("direct:getAllTopics")
                .get("/{id}").routeId("getTopicById").to("direct:getTopicById")
                .post().type(TopicDTO.class).routeId("createTopic").to("direct:createTopic")
                .put("/{id}").type(TopicDTO.class).routeId("updateTopic").to("direct:updateTopic")
                .delete("/{id}").routeId("deleteTopic").to("direct:deleteTopic");

        from("direct:getAllTopics")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    List<TopicDTO> topics = topicService.getAllTopics();
                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "All topics retrieved successfully", topics);
                    exchange.getMessage().setBody(apiResponseDTO);
                });

        from("direct:getTopicById")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    Long id = Long.parseLong(exchange.getIn().getHeader("id").toString());
                    TopicDTO topic = topicService.getTopicById(id);
                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with id " + id + " retrieved successfully", topic);
                    exchange.getMessage().setBody(apiResponseDTO);
                });

        from("direct:createTopic")
                .routeId("processCreateTopic")
                .process(exchange -> {
                    TopicDTO topicDTO = exchange.getIn().getBody(TopicDTO.class);
                    TopicDTO topicCreated = topicService.createTopic(topicDTO);
                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(), "Topic with name " + topicCreated.getName() + " created successfully", topicCreated);
                    exchange.getMessage().setBody(apiResponseDTO);
                });


    }
}
