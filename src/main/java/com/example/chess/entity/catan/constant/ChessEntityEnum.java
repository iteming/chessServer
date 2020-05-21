package com.example.chess.entity.catan.constant;

public enum ChessEntityEnum {
    VILLAGE(1, "村庄"),
    ROAD(2, "道路"),
    CITY(3, "城市"),
    CARDS(4, "发展卡"),
    ;

    ChessEntityEnum(int code, String name) {
        this.name = name;
    }

    private int code;

    private String name;

    public String getName() {
        return name;
    }
}
