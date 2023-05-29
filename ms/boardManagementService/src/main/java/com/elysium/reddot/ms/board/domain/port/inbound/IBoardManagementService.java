package com.elysium.reddot.ms.board.domain.port.inbound;


import com.elysium.reddot.ms.board.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * Interface for managing board models.
 */
public interface IBoardManagementService {

    /**
     * Get a board model by its unique identifier.
     *
     * @param id The unique identifier of the board model to fetch.
     * @return The board model with the specified id.
     */
    @ApiOperation(value = "Retrieve a board by ID")
    BoardModel getBoardById(Long id);

    /**
     * Get all board models.
     *
     * @return A list of all board models.
     */
    @ApiOperation(value = "Retrieve all boards")
    List<BoardModel> getAllBoards();

    /**
     * Create a new board model.
     *
     * @param boardModel The board model to be created.
     * @return The created board model.
     * @throws FieldEmptyException If any required field in the board model is empty.
     */
    @ApiOperation(value = "Create a new board")
    BoardModel createBoard(BoardModel boardModel) throws FieldEmptyException;

    /**
     * Update a board model with a given identifier.
     *
     * @param id The identifier of the board model to update.
     * @param boardModel The updated board model.
     * @return The updated board model.
     */
    @ApiOperation(value = "Update an existing board")
    BoardModel updateBoard(Long id, BoardModel boardModel);


}
