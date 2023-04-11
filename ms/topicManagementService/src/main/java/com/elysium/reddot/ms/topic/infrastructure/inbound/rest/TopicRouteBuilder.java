package com.elysium.reddot.ms.topic.infrastructure.inbound.rest;


import com.elysium.reddot.ms.topic.application.service.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TopicRouteBuilder extends RouteBuilder {

    private final TopicApplicationServiceImpl topicService;

    public TopicRouteBuilder(TopicApplicationServiceImpl topicService) {
        this.topicService = topicService;
    }

    @Override
    public void configure() {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

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
                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO("All topics retrieved successfully", topics);
                    exchange.getMessage().setBody(apiResponseDTO);
                });

        from("direct:getTopicById")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    Long id = Long.parseLong(exchange.getIn().getHeader("id").toString());
                    Optional<TopicDTO> topic = topicService.getTopicById(id);
                    ApiResponseDTO apiResponseDTO = new ApiResponseDTO("Topic with id " + id + " retrieved successfully", topic.get());
                    exchange.getMessage().setBody(apiResponseDTO);
                });


    }
}
