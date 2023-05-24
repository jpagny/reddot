package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.mapper.BoardProcessorMapper;
import com.elysium.reddot.ms.board.infrastructure.outbound.rabbitmq.requester.TopicExistRequester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateBoardProcessor implements Processor {

    private final BoardApplicationServiceImpl boardApplicationService;
    private final TopicExistRequester topicExistRequester;

    @Override
    public void process(Exchange exchange) throws Exception {
        BoardDTO inputBoardDTO = exchange.getIn().getBody(BoardDTO.class);
        BoardModel boardModel = BoardProcessorMapper.toModel(inputBoardDTO);

        topicExistRequester.verifyTopicIdExistsOrThrow(boardModel.getTopicId());

        createBoardAndSetResponse(exchange, boardModel);
    }

    private void createBoardAndSetResponse(Exchange exchange, BoardModel boardModel) {
        BoardModel createdBoardModel = boardApplicationService.createBoard(boardModel);

        BoardDTO createdBoardDTO = BoardProcessorMapper.toDTO(createdBoardModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Board with name " + createdBoardModel.getName() + " created successfully", createdBoardDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }


}