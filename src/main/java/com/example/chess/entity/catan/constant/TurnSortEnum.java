package com.example.chess.entity.catan.constant;


/**
 * @version 1.0.0
 * @author: jiangbin
 * @description 状态码枚举类
 **/
public enum TurnSortEnum {

    ASC(1, "正序"),
    DESC(2, "逆序"),
    ;


    TurnSortEnum(int code, String name) {
        this.name = name;
    }

    private int code;

    private String name;

    public String getName() {
        return name;
    }
}
