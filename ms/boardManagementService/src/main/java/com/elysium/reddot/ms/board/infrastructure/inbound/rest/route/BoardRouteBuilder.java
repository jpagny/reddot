package com.elysium.reddot.ms.board.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.board.infrastructure.constant.BoardRouteConstants;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.BoardProcessorHolder;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoardRouteBuilder extends RouteBuilder {

    private final CamelGlobalExceptionHandler camelGlobalExceptionHandler;
    private final BoardProcessorHolder boardProcessorHolder;

    @Override
    public void configure() {

        String requestId = "/{id}";

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        onException(Exception.class)
                .handled(true)
                .process(camelGlobalExceptionHandler);


        // definition routes
        rest().
                get().to(BoardRouteConstants.GET_ALL_BOARDS.getRouteName())
                .get(requestId).to(BoardRouteConstants.GET_BOARD_BY_ID.getRouteName())
                .post().type(BoardDTO.class).to(BoardRouteConstants.CREATE_BOARD.getRouteName())
                .put(requestId).type(BoardDTO.class).to(BoardRouteConstants.UPDATE_BOARD.getRouteName())
                .delete(requestId).to(BoardRouteConstants.DELETE_BOARD.getRouteName());

        // route : get all boards
        from(BoardRouteConstants.GET_ALL_BOARDS.getRouteName())
                .routeId("getAllBoards")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all boards")
                .process(boardProcessorHolder.getGetAllBoardsProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all boards")
                .end();

        // route : get board by id
        from(BoardRouteConstants.GET_BOARD_BY_ID.getRouteName())
                .routeId("getBoardById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting board with id '${header.id}'")
                .process(boardProcessorHolder.getGetBoardByIdProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved board with id '${header.id}'")
                .end();

        // route : create board
        from(BoardRouteConstants.CREATE_BOARD.getRouteName())
                .routeId("createBoard")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new board")
                .process(boardProcessorHolder.getCreateBoardProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created board with name '${body.data.name}'")
                .end();

        // route : update board
        from(BoardRouteConstants.UPDATE_BOARD.getRouteName())
                .routeId("updateBoard")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating board with id '${header.id}'")
                .process(boardProcessorHolder.getUpdateBoardProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated board with id '${header.id}' and name '${body.data.name}'")
                .end();

        // route : delete board
        from(BoardRouteConstants.DELETE_BOARD.getRouteName())
                .routeId("deleteBoard")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Deleting board with id '${header.id}'")
                .process(boardProcessorHolder.getDeleteBoardProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully deleted board with id '${header.id}'")
                .end();

    }
}
