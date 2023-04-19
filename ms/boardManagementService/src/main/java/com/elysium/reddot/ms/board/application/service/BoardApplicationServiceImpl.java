package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.data.mapper.BoardApplicationMapper;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.application.port.in.IBoardManagement;
import com.elysium.reddot.ms.board.application.port.out.IBoardRepositoryOutbound;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BoardApplicationServiceImpl implements IBoardApplicationService {

    private static final String RESOURCE_NAME_BOARD = "board";
    private final IBoardManagement boardDomainService;
    private final IBoardRepositoryOutbound boardRepositoryOutbound;

    @Autowired
    public BoardApplicationServiceImpl(IBoardManagement boardDomainService, IBoardRepositoryOutbound boardRepositoryOutbound) {
        this.boardDomainService = boardDomainService;
        this.boardRepositoryOutbound = boardRepositoryOutbound;
    }

    public BoardDTO getBoardById(Long id) {
        log.debug("Fetching board with id {}", id);

        BoardDTO boardDTO = boardRepositoryOutbound.findBoardById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_BOARD, String.valueOf(id))
        );

        log.info("Successfully retrieved board with id {}, name '{}', description '{}'",
                id, boardDTO.getName(), boardDTO.getDescription());

        return boardDTO;
    }

    @Override
    public List<BoardDTO> getAllBoards() {
        log.info("Fetching all boards from database...");

        return boardRepositoryOutbound.findAllBoards()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Override
    public BoardDTO createBoard(BoardDTO boardToCreateDTO) {

        log.debug("Creating new board with name '{}', label '{}, description '{}'",
                boardToCreateDTO.getName(),
                boardToCreateDTO.getLabel(),
                boardToCreateDTO.getDescription());

        Optional<BoardDTO> existingBoard = boardRepositoryOutbound.findBoardByName(boardToCreateDTO.getName());

        if (existingBoard.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_BOARD, "name", boardToCreateDTO.getName());
        }

        BoardModel boardModel = BoardApplicationMapper.toModel(boardToCreateDTO);

        try {
            boardDomainService.validateBoardForCreation(boardModel);
        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_BOARD, exception.getMessage());
        }

        BoardDTO boardCreatedDTO = boardRepositoryOutbound.createBoard(boardToCreateDTO);

        log.info("Successfully created board with id {}, name '{}', label '{}' description '{}'",
                boardCreatedDTO.getId(),
                boardCreatedDTO.getName(),
                boardCreatedDTO.getLabel(),
                boardCreatedDTO.getDescription());

        return boardCreatedDTO;
    }

    @Override
    public BoardDTO updateBoard(Long id, BoardDTO boardToUpdateDTO) {
        log.debug("Updating board with id '{}', name '{}', label '{}', description '{}'",
                id, boardToUpdateDTO.getName(), boardToUpdateDTO.getLabel(), boardToUpdateDTO.getDescription());

        BoardDTO existingBoardDTO = boardRepositoryOutbound.findBoardById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_BOARD, String.valueOf(id))
        );

        BoardModel existingBoardModel = BoardApplicationMapper.toModel(existingBoardDTO);
        BoardModel boardToUpdateModel = BoardApplicationMapper.toModel(boardToUpdateDTO);

        try {
            BoardModel boardUpdatedModel = boardDomainService.updateExistingBoardWithUpdates(existingBoardModel, boardToUpdateModel);
            BoardDTO boardUpdatedDTO = BoardApplicationMapper.toDTO(boardUpdatedModel);

            boardUpdatedDTO = boardRepositoryOutbound.updateBoard(boardUpdatedDTO);

            log.info("Successfully updated board with id '{}', name '{}', label'{}, description '{}'",
                    boardUpdatedDTO.getId(),
                    boardUpdatedDTO.getName(),
                    boardUpdatedDTO.getLabel(),
                    boardUpdatedDTO.getDescription());

            return boardUpdatedDTO;

        } catch (Exception ex) {
            throw new ResourceBadValueException(RESOURCE_NAME_BOARD, ex.getMessage());

        }
    }

    @Override
    public void deleteBoardById(Long id) throws ResourceNotFoundException {
        log.debug("Deleting board with id {}", id);

        BoardDTO boardToDelete = boardRepositoryOutbound.findBoardById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_BOARD, String.valueOf(id))
        );

        boardRepositoryOutbound.deleteBoard(id);

        log.info("Successfully deleted board with id '{}', name '{}', description '{}'",
                boardToDelete.getId(), boardToDelete.getName(), boardToDelete.getDescription());
    }

}