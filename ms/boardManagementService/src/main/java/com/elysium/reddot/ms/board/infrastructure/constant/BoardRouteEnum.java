package com.elysium.reddot.ms.board.infrastructure.constant;

/**
 * Enum representing the various routes for board-related actions.
 */
public enum BoardRouteEnum {

    GET_ALL_BOARDS("direct:getAllBoards"),
    GET_BOARD_BY_ID("direct:getBoardById"),
    CREATE_BOARD("direct:createBoard"),
    UPDATE_BOARD("direct:updateBoard"),
    DELETE_BOARD("direct:deleteBoard");

    private final String routeName;

    BoardRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
