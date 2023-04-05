package com.reddot.ms.topic.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicGetAllProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("topics","hello");
        exchange.getMessage().setBody(result);
    }
}
