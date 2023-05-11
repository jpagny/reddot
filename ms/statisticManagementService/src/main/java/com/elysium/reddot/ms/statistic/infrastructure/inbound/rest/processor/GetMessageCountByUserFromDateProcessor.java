package com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.statistic.application.data.ApiResponseDTO;
import com.elysium.reddot.ms.statistic.application.service.StatisticApplicationServiceImpl;
import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class GetMessageCountByUserFromDateProcessor implements Processor {

    private final StatisticApplicationServiceImpl statisticApplicationService;

    @Override
    public void process(Exchange exchange) {

        String type = exchange.getIn().getHeader("type", String.class);
        String userId = exchange.getIn().getHeader("userId", String.class);
        String date = exchange.getIn().getHeader("date", String.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate theDate = LocalDate.parse(date, formatter);

        Integer resultMessageCount = statisticApplicationService.getMessageCountByTypeAndByUserIdFromDate(type, userId, theDate);

        UserMessageStatisticModel result = new UserMessageStatisticModel();
        result.setDate(theDate);
        result.setUserId(userId);
        result.setTypeStatistic(type);
        result.setCountMessages(resultMessageCount);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message count with  " + type + " retrieved successfully", result);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }


}
