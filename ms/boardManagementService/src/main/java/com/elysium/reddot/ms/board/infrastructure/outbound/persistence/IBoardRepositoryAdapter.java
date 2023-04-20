package com.elysium.reddot.ms.board.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.domain.port.outbound.IBoardRepository;
import com.elysium.reddot.ms.board.infrastructure.mapper.BoardPersistenceMapper;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.repository.BoardJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IBoardRepositoryAdapter implements IBoardRepository {

    private final BoardJpaRepository boardJpaRepository;

    public IBoardRepositoryAdapter(BoardJpaRepository boardJpaRepository) {
        this.boardJpaRepository = boardJpaRepository;
    }

    @Override
    public BoardModel createBoard(BoardModel boardModel) {
        BoardJpaEntity boardEntity = BoardPersistenceMapper.toEntity(boardModel);
        BoardJpaEntity savedBoard = boardJpaRepository.save(boardEntity);
        return BoardPersistenceMapper.toModel(savedBoard);
    }

    @Override
    public Optional<BoardModel> findBoardById(Long id) {
        return boardJpaRepository.findById(id)
                .map(BoardPersistenceMapper::toModel);
    }

    @Override
    public Optional<BoardModel> findBoardByName(String name) {
        return boardJpaRepository.findByName(name)
                .map(BoardPersistenceMapper::toModel);
    }

    @Override
    public List<BoardModel> findAllBoards() {
        return boardJpaRepository.findAll()
                .stream()
                .map(BoardPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public BoardModel updateBoard(BoardModel boardModel) {
        BoardJpaEntity boardEntity = BoardPersistenceMapper.toEntity(boardModel);
        BoardJpaEntity updatedBoard = boardJpaRepository.save(boardEntity);
        return BoardPersistenceMapper.toModel(updatedBoard);
    }

    @Override
    public void deleteBoard(Long id) {
        boardJpaRepository.deleteById(id);
    }
}
