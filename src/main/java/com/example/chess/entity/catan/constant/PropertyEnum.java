package com.example.chess.entity.catan.constant;


/**
 * @version 1.0.0
 * @author: jiangbin
 * @description 状态码枚举类
 **/
public enum PropertyEnum {

    /**
     * 沙漠
     */
    DESERT(-1, "沙漠"),
    WATER(0, "水"),
    TREE(1, "树"),
    /**
     * 砖
     */
    BRICK(2, "砖"),
    SHEEP(3, "羊"),
    /**
     * 麦子
     */
    WHEAT(4, "麦子"),
    STONE(4, "石头"),
    ANY_THING(10, "任意"),
    ;


    PropertyEnum(int code, String name) {
        this.name = name;
    }

    private int code;

    private String name;

    public String getName() {
        return name;
    }
}
