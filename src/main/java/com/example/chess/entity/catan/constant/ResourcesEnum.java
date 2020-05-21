package com.example.chess.entity.catan.constant;

public enum ResourcesEnum {

    TREE(1, "树"),
    BRICK(2, "砖"),
    SHEEP(3, "羊"),
    WHEAT(4, "麦子"),
    STONE(4, "石头"),
    ;

    ResourcesEnum(int code, String name) {
        this.name = name;
    }

    private int code;

    private String name;

    public String getName() {
        return name;
    }
}
