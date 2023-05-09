package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.domain.port.outbound.IBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardRabbitMQService {

    private final IBoardRepository boardRepository;

    public boolean checkBoardIdExists(Long id) {
        Optional<BoardModel> topicModel = boardRepository.findBoardById(id);
        return topicModel.isPresent();
    }


}
