package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.data.mapper.BoardDTOBoardModelMapper;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Processor class for retrieving a board by its ID.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetBoardByIdProcessor implements Processor {

    private final BoardApplicationServiceImpl boardService;

    /**
     * Processes the incoming exchange to retrieve a board by its ID.
     *
     * @param exchange the incoming exchange
     */
    @Override
    public void process(Exchange exchange) {
        log.info("Processing GetBoardByIdProcessor...");

        Long inputId = Long.parseLong(exchange.getIn().getHeader("id").toString());

        log.debug("Retrieving board by ID: {}", inputId);
        BoardModel boardModel = boardService.getBoardById(inputId);

        BoardDTO boardDTO = BoardDTOBoardModelMapper.toDTO(boardModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id " + inputId + " retrieved successfully", boardDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.info("Retrieved board successfully. Board ID: {}", inputId);
    }

}