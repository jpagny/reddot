package com.elysium.reddot.ms.board.domain.port.outbound;

import com.elysium.reddot.ms.board.domain.model.BoardModel;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the board repository.
 * Provides methods for performing CRUD operations on BoardModel objects.
 */
public interface IBoardRepository {

    /**
     * Creates a new BoardModel.
     *
     * @param boardModel the BoardModel to be created
     * @return the newly created BoardModel
     */
    BoardModel createBoard(BoardModel boardModel);

    /**
     * Retrieves a specific BoardModel by its unique identifier.
     *
     * @param id the unique identifier of the board to be retrieved
     * @return an Optional containing the BoardModel if found, otherwise an empty Optional
     */
    Optional<BoardModel> findBoardById(Long id);

    /**
     * Retrieves a specific BoardModel by its name.
     *
     * @param name the name of the board to be retrieved
     * @return an Optional containing the BoardModel if found, otherwise an empty Optional
     */
    Optional<BoardModel> findBoardByName(String name);

    /**
     * Retrieves all BoardModels.
     *
     * @return a list of all BoardModels
     */
    List<BoardModel> findAllBoards();

    /**
     * Updates a specific BoardModel.
     *
     * @param boardModel the BoardModel containing updated information
     * @return the updated BoardModel
     */
    BoardModel updateBoard(BoardModel boardModel);

}
