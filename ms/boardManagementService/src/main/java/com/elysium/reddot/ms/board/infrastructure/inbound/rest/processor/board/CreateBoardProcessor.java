package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.data.mapper.BoardDTOBoardModelMapper;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.outbound.rabbitmq.requester.TopicExistRequester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Processor class for creating a new board.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateBoardProcessor implements Processor {

    private final BoardApplicationServiceImpl boardApplicationService;
    private final TopicExistRequester topicExistRequester;

    /**
     * Processes the incoming exchange to create a new board.
     * Validates if the associated topic exists and then creates a new board.
     *
     * @param exchange the incoming exchange
     * @throws Exception if any error occurs during the processing
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Processing CreateBoardProcessor...");

        BoardDTO inputBoardDTO = exchange.getIn().getBody(BoardDTO.class);
        BoardModel boardModel = BoardDTOBoardModelMapper.toModel(inputBoardDTO);

        log.debug("Verifying if the associated topic exists for board creation...");
        topicExistRequester.verifyTopicIdExistsOrThrow(boardModel.getTopicId());

        createBoardAndSetResponse(exchange, boardModel);
    }

    /**
     * Creates a new board and sets the ApiResponseDTO response to the exchange.
     *
     * @param exchange   the incoming exchange
     * @param boardModel the board model to be created
     */
    private void createBoardAndSetResponse(Exchange exchange, BoardModel boardModel) {
        log.debug("Creating a new board...");

        BoardModel createdBoardModel = boardApplicationService.createBoard(boardModel);

        BoardDTO createdBoardDTO = BoardDTOBoardModelMapper.toDTO(createdBoardModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Board with name " + createdBoardModel.getName() + " created successfully", createdBoardDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.info("Created board successfully. Board ID: {}", createdBoardModel.getId());
    }


}