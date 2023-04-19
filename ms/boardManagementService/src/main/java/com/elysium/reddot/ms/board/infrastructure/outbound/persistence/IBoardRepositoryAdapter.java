package com.elysium.reddot.ms.board.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.port.out.IBoardRepositoryOutbound;
import com.elysium.reddot.ms.board.infrastructure.mapper.BoardInfrastructureMapper;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.repository.BoardJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IBoardRepositoryAdapter implements IBoardRepositoryOutbound {

    private final BoardJpaRepository boardJpaRepository;

    public IBoardRepositoryAdapter(BoardJpaRepository boardJpaRepository) {
        this.boardJpaRepository = boardJpaRepository;
    }

    @Override
    public BoardDTO createBoard(BoardDTO boardDto) {
        BoardJpaEntity boardEntity = BoardInfrastructureMapper.toEntity(boardDto);
        BoardJpaEntity savedBoard = boardJpaRepository.save(boardEntity);
        return BoardInfrastructureMapper.toDTO(savedBoard);
    }

    @Override
    public Optional<BoardDTO> findBoardById(Long id) {
        return boardJpaRepository.findById(id)
                .map(BoardInfrastructureMapper::toDTO);
    }

    @Override
    public Optional<BoardDTO> findBoardByName(String name) {
        return boardJpaRepository.findByName(name)
                .map(BoardInfrastructureMapper::toDTO);
    }

    @Override
    public List<BoardDTO> findAllBoards() {
        return boardJpaRepository.findAll()
                .stream()
                .map(BoardInfrastructureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BoardDTO updateBoard(BoardDTO boardDto) {
        BoardJpaEntity boardEntity = BoardInfrastructureMapper.toEntity(boardDto);
        BoardJpaEntity updatedBoard = boardJpaRepository.save(boardEntity);
        return BoardInfrastructureMapper.toDTO(updatedBoard);
    }

    @Override
    public void deleteBoard(Long id) {
        boardJpaRepository.deleteById(id);
    }
}
