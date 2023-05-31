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

import java.util.List;

/**
 * Processor class for retrieving all boards.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllBoardsProcessor implements Processor {

    private final BoardApplicationServiceImpl boardService;

    /**
     * Processes the incoming exchange to retrieve all boards.
     *
     * @param exchange the incoming exchange
     */
    @Override
    public void process(Exchange exchange) {
        log.info("Processing GetAllBoardsProcessor...");

        List<BoardModel> listBoardsModel = boardService.getAllBoards();

        List<BoardDTO> listBoardsDTO = BoardDTOBoardModelMapper.toDTOList(listBoardsModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "All boards retrieved successfully", listBoardsDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.info("Retrieved all boards successfully. Count: {}", listBoardsModel.size());
    }
}
