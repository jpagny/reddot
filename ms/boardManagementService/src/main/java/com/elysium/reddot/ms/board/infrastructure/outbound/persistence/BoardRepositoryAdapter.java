package com.elysium.reddot.ms.board.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.domain.port.outbound.IBoardRepository;
import com.elysium.reddot.ms.board.application.data.mapper.BoardJpaBoardModelMapper;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.repository.BoardJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BoardRepositoryAdapter implements IBoardRepository {

    private final BoardJpaRepository boardJpaRepository;

    public BoardRepositoryAdapter(BoardJpaRepository boardJpaRepository) {
        this.boardJpaRepository = boardJpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardModel createBoard(BoardModel boardModel) {
        BoardJpaEntity boardEntity = BoardJpaBoardModelMapper.toEntity(boardModel);
        BoardJpaEntity savedBoard = boardJpaRepository.save(boardEntity);
        return BoardJpaBoardModelMapper.toModel(savedBoard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BoardModel> findBoardById(Long id) {
        return boardJpaRepository.findById(id)
                .map(BoardJpaBoardModelMapper::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BoardModel> findFirstByNameAndTopicId(String name, Long topicId) {
        return boardJpaRepository.findFirstByNameAndTopicId(name, topicId)
                .map(BoardJpaBoardModelMapper::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BoardModel> findAllBoards() {
        return boardJpaRepository.findAll()
                .stream()
                .map(BoardJpaBoardModelMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardModel updateBoard(BoardModel boardModel) {
        BoardJpaEntity boardEntity = BoardJpaBoardModelMapper.toEntity(boardModel);
        BoardJpaEntity updatedBoard = boardJpaRepository.save(boardEntity);
        return BoardJpaBoardModelMapper.toModel(updatedBoard);
    }

}
