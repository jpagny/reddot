package com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.statistic.application.data.ApiResponseDTO;
import com.elysium.reddot.ms.statistic.application.service.StatisticApplicationServiceImpl;
import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMessageCountByUserFromDateProcessor implements Processor {

    private final StatisticApplicationServiceImpl statisticApplicationService;

    @Override
    public void process(Exchange exchange) {
        log.info("Processing the exchange in GetMessageCountByUserFromDateProcessor...");

        String type = exchange.getIn().getHeader("type", String.class);
        String userId = exchange.getIn().getHeader("userId", String.class);
        String date = exchange.getIn().getHeader("date", String.class);

        log.debug("Retrieved type, userId, and date from the headers: {}, {}, {}", type, userId, date);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate theDate = LocalDate.parse(date, formatter);

        Integer resultMessageCount = statisticApplicationService.getMessageCountByTypeAndByUserIdFromDate(type, userId, theDate);

        log.debug("Retrieved message count: {}", resultMessageCount);

        UserMessageStatisticModel result = new UserMessageStatisticModel(
                theDate,
                resultMessageCount,
                type,
                userId
        );

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message count with  " + type + " retrieved successfully", result);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.info("Finished processing the exchange in GetMessageCountByUserFromDateProcessor.");
    }


}
