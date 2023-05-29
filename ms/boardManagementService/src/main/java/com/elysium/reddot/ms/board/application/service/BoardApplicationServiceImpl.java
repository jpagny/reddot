package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.board.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.domain.port.inbound.IBoardManagementService;
import com.elysium.reddot.ms.board.domain.port.outbound.IBoardRepository;
import com.elysium.reddot.ms.board.domain.service.BoardDomainServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link IBoardManagementService} interface, providing methods for managing Board objects.
 */
@Api(tags = "Board Management")
@Service
@Transactional
@Slf4j
public class BoardApplicationServiceImpl implements IBoardManagementService {

    private static final String RESOURCE_NAME_BOARD = "board";
    private final BoardDomainServiceImpl boardDomainService;
    private final IBoardRepository boardRepository;

    /**
     * Creates a new BoardApplicationServiceImpl.
     *
     * @param boardRepository the repository for interacting with the database
     */
    @Autowired
    public BoardApplicationServiceImpl(IBoardRepository boardRepository) {
        this.boardDomainService = new BoardDomainServiceImpl();
        this.boardRepository = boardRepository;
    }

    /**
     * {@inheritDoc}
     */
    @ApiOperation(value = "Retrieve a board by ID")
    @Override
    public BoardModel getBoardById(Long id) {
        log.debug("Fetching board with id {}", id);

        BoardModel foundBoardModel = boardRepository.findBoardById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_BOARD, String.valueOf(id))
        );

        log.info("Successfully retrieved board with id {}, name '{}', description '{}'",
                id, foundBoardModel.getName(), foundBoardModel.getDescription());

        return foundBoardModel;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<BoardModel> getAllBoards() {
        log.info("Fetching all boards from database...");

        return boardRepository.findAllBoards()
                .parallelStream()
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardModel createBoard(BoardModel boardToCreateModel) {

        log.debug("Creating new board with name '{}', label '{}, description '{}'",
                boardToCreateModel.getName(),
                boardToCreateModel.getLabel(),
                boardToCreateModel.getDescription());

        Optional<BoardModel> existingBoard = boardRepository
                .findFirstByNameAndTopicId(boardToCreateModel.getName(), boardToCreateModel.getTopicId());

        if (existingBoard.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_BOARD,
                    "name",
                    boardToCreateModel.getName(),
                    boardToCreateModel.getTopicId());
        }

        try {
            boardDomainService.validateBoardForCreation(boardToCreateModel);
        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_BOARD, exception.getMessage());
        }

        BoardModel createdBoardModel = boardRepository.createBoard(boardToCreateModel);

        log.info("Successfully created board with id {}, name '{}', label '{}' description '{}'",
                createdBoardModel.getId(),
                createdBoardModel.getName(),
                createdBoardModel.getLabel(),
                createdBoardModel.getDescription());

        return createdBoardModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardModel updateBoard(Long id, BoardModel boardToUpdateModel) {
        log.debug("Updating board with id '{}', name '{}', label '{}', description '{}'",
                id, boardToUpdateModel.getName(), boardToUpdateModel.getLabel(), boardToUpdateModel.getDescription());

        BoardModel existingBoardModel = boardRepository.findBoardById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_BOARD, String.valueOf(id))
        );

        try {
            BoardModel boardModelWithUpdates = boardDomainService.updateExistingBoardWithUpdates(existingBoardModel, boardToUpdateModel);

            BoardModel updatedBoardModel = boardRepository.updateBoard(boardModelWithUpdates);

            log.info("Successfully updated board with id '{}', name '{}', label'{}, description '{}'",
                    updatedBoardModel.getId(),
                    updatedBoardModel.getName(),
                    updatedBoardModel.getLabel(),
                    updatedBoardModel.getDescription());

            return updatedBoardModel;

        } catch (Exception ex) {
            throw new ResourceBadValueException(RESOURCE_NAME_BOARD, ex.getMessage());

        }
    }


}