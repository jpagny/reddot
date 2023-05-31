package com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.processor.StatisticProcessorHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

/**
 * A Camel RouteBuilder that defines the route for handling statistics in the application.
 **/
@Component
@AllArgsConstructor
public class StatisticRouteBuilder extends RouteBuilder {

    private final StatisticProcessorHolder statisticProcessorHolder;
    private final ObjectMapper objectMapper;

    @Override
    public void configure() {

        JacksonDataFormat format = new JacksonDataFormat();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        format.setObjectMapper(objectMapper);

        restConfiguration()
                .component("servlet")
                .dataFormatProperty("prettyPrint", "true");

        // definition routes
        rest()
                .get("/count/{userId}/{type}/{date}/").to(StatisticRouteConstants.GET_MESSAGE_COUNT_BY_TYPE_AND_BY_USER_FROM_DATE.getRouteName());

        from(StatisticRouteConstants.GET_MESSAGE_COUNT_BY_TYPE_AND_BY_USER_FROM_DATE.getRouteName())
                .routeId("getMessageCountByUserFromDate")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving message count by user from date")
                .process(statisticProcessorHolder.getGetMessageCountByUserFromDateProcessor())
                .marshal(format)
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved message count")
                .end();

    }

}