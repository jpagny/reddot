package com.elysium.reddot.ms.board.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.infrastructure.constant.BoardRouteEnum;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board.BoardProcessorHolder;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardRouteBuilder extends RouteBuilder {

    private final GlobalExceptionHandler globalExceptionHandler;
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
                .process(globalExceptionHandler);


        // definition routes
        rest("").
                get().to(BoardRouteEnum.GET_ALL_BOARDS.getRouteName())
                .get(requestId).to(BoardRouteEnum.GET_BOARD_BY_ID.getRouteName())
                .post().type(BoardDTO.class).to(BoardRouteEnum.CREATE_BOARD.getRouteName())
                .put(requestId).type(BoardDTO.class).to(BoardRouteEnum.UPDATE_BOARD.getRouteName())
                .delete(requestId).to(BoardRouteEnum.DELETE_BOARD.getRouteName());

        // route : get all boards
        from(BoardRouteEnum.GET_ALL_BOARDS.getRouteName())
                .routeId("getAllBoards")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Retrieving all boards")
                .process(boardProcessorHolder.getGetAllBoardsProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved all boards")
                .end();

        // route : get board by id
        from(BoardRouteEnum.GET_BOARD_BY_ID.getRouteName())
                .routeId("getBoardById")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Getting board with id '${header.id}'")
                .process(boardProcessorHolder.getGetBoardByIdProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully retrieved board with id '${header.id}'")
                .end();

        // route : create board
        from(BoardRouteEnum.CREATE_BOARD.getRouteName())
                .routeId("createBoard")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Creating a new board")
                .policy("adminPolicy")
                .process(boardProcessorHolder.getCreateBoardProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully created board with name '${body.data.name}'")
                .end();

        // route : update board
        from(BoardRouteEnum.UPDATE_BOARD.getRouteName())
                .routeId("updateBoard")
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Updating board with id '${header.id}'")
                .policy("adminPolicy")
                .process(boardProcessorHolder.getUpdateBoardProcessor())
                .log("Route '${routeId}': Path '${header.CamelHttpUri}': Successfully updated board with id '${header.id}' and name '${body.data.name}'")
                .end();

    }


}
