package com.elysium.reddot.ms.board.infrastructure.inbound.rest.route;

public enum BoardRouteConstants {

    GET_ALL_BOARDS("direct:getAllBoards"),
    GET_BOARD_BY_ID("direct:getBoardById"),
    CREATE_BOARD("direct:createBoard"),
    UPDATE_BOARD("direct:updateBoard"),
    DELETE_BOARD("direct:deleteBoard");

    private final String routeName;

    BoardRouteConstants(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
