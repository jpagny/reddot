package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.mapper.BoardProcessorMapper;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateBoardProcessor implements Processor {

    private final BoardApplicationServiceImpl boardService;

    @Override
    public void process(Exchange exchange) {
        Long inputId = exchange.getIn().getHeader("id", Long.class);
        BoardDTO inputBoardDTO = exchange.getIn().getBody(BoardDTO.class);
        BoardModel boardToUpdateModel = BoardProcessorMapper.toModel(inputBoardDTO);

        BoardModel updatedBoardModel = boardService.updateBoard(inputId, boardToUpdateModel);

        BoardDTO updatedBoardDTO = BoardProcessorMapper.toDTO(updatedBoardModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Board with name " + updatedBoardDTO.getName() + " updated successfully", updatedBoardDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
