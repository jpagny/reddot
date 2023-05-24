package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class BoardProcessorHolder {
    private final GetAllBoardsProcessor getAllBoardsProcessor;
    private final GetBoardByIdProcessor getBoardByIdProcessor;
    private final CreateBoardProcessor createBoardProcessor;
    private final UpdateBoardProcessor updateBoardProcessor;
    private final DeleteBoardProcessor deleteBoardProcessor;
}