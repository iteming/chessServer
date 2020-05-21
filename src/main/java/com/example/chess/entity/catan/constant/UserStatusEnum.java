package com.example.chess.entity.catan.constant;

public enum UserStatusEnum {

    // 0 等待其他人，1 等待掷筛子，2 等待建造， 3 等待弃一半牌， 4 等待执行限制棋， 5 等待夺取牌， 6 等待贸易确认
    WAIT(0, "等待其他人"),
    THROW_SIEVE(1, "等待掷筛子"),
    BUILD(2, "等待建造"),
    THROW_CARDS(3, "等待弃一半牌"),
    MOVE_LIMIT_CHESS(4, "等待执行限制棋"),
    TAKE_CARDS(5, "等待夺取牌"),
    TRADE(5, "等待贸易确认"),
    ;

    UserStatusEnum(int code, String name) {
        this.name = name;
    }

    private int code;

    private String name;

    public String getName() {
        return name;
    }
}
