package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.BoardRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for handling operations related to RabbitMQ in context of Boards.
 */
@Service
@RequiredArgsConstructor
public class BoardRabbitMQService {

    private final BoardRepositoryAdapter boardRepository;

    /**
     * Checks if a board with the provided ID exists.
     *
     * @param id the ID of the board to check
     * @return true if the board exists, false otherwise
     */
    public boolean checkBoardIdExists(Long id) {
        Optional<BoardModel> boardModel = boardRepository.findBoardById(id);
        return boardModel.isPresent();
    }


}
