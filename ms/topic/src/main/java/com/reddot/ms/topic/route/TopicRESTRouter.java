package com.reddot.ms.topic.route;

import com.reddot.ms.topic.dto.TopicDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class TopicRESTRouter extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration()
                .bindingMode(RestBindingMode.json);
        log.debug("ICII ROUTE EST PASSE");
        rest("/topics").description("Todo REST service")
                .consumes("application/json")
                .produces("application/json")

                .get()
                .description("Find all topics")
                .outType(TopicDTO[].class)
                .responseMessage().code(200).message("All topics successfully returned").endResponseMessage()
                .to("bean:topicService?method=getAll");

    }
}