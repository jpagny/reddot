package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteBoardProcessor implements Processor {

    private final BoardApplicationServiceImpl boardService;

    @Override
    public void process(Exchange exchange) {
        Long id = exchange.getIn().getHeader("id", Long.class);
        BoardDTO boardDTO = boardService.getBoardById(id);

        boardService.deleteBoardById(id);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Board with id " + id + " deleted successfully", boardDTO);
        exchange.getMessage().setBody(apiResponseDTO);
    }
}