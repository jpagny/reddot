package com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.processor.StatisticProcessorHolder;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StatisticRouteBuilder extends RouteBuilder {

    private final StatisticProcessorHolder statisticProcessorHolder;

    @Override
    public void configure() {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        // definition routes
        rest()
                .get("/count/{userId}/{type}/{date}/").to(StatisticRouteConstants.GET_MESSAGE_COUNT_BY_TYPE_AND_BY_USER_FROM_DATE.getRouteName());

        from(StatisticRouteConstants.GET_MESSAGE_COUNT_BY_TYPE_AND_BY_USER_FROM_DATE.getRouteName())
                .routeId("getMessageCountByUserFromDate")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving message count by user from date")
                .process(statisticProcessorHolder.getGetMessageCountByUserFromDateProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved message count")
                .end();

    }

}