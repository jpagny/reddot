package com.elysium.reddot.ms.topic.infrastructure.adapter.inbound.rest;


import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.port.in.TopicUseCase;
import com.elysium.reddot.ms.topic.application.response.ApiResult;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TopicRouteBuilder extends RouteBuilder {

    private final TopicUseCase topicUseCase;

    public TopicRouteBuilder(TopicUseCase topicUseCase) {
        this.topicUseCase = topicUseCase;
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
                    List<TopicDTO> topics = topicUseCase.getAllTopics();
                    ApiResult apiResult = new ApiResult("All topics retrieved successfully", topics);
                    exchange.getMessage().setBody(apiResult);
                });

        from("direct:getTopicById")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    Long id = Long.parseLong(exchange.getIn().getHeader("id").toString());
                    Optional<TopicDTO> topic = topicUseCase.getTopicById(id);
                    ApiResult apiResult = new ApiResult("Topic with id " + id + " retrieved successfully", topic);
                    exchange.getMessage().setBody(apiResult);
                });


    }
}
