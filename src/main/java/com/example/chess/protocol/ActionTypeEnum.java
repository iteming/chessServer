package com.example.chess.protocol;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @version 1.0.0
 * @author: jiangbin
 * @description 状态码枚举类
 **/
public enum ActionTypeEnum implements ICode {

    LOGIN(1, "登录"),
    CONNECT_START(2, "连接房间"),

    MATCH_START(3, "开始匹配"),
    MATCH_CANCEL(4, "取消匹配"),

    READY_START(5, "准备"),
    READY_CANCEL(6, "取消准备"),

    GAME_START(7, "游戏开始"),
    GAME_END(8, "游戏结束"),

    TURN_ROUND(10, "回合流转"),
    TURN_ROUND_THROW_SIEVE(11, "投掷筛子"),
    TURN_ROUND_ADD_RESOURCES(12, "加资源"),
    TURN_ROUND_THROW_CARDS(13, "扔掉一半手牌"),
    TURN_ROUND_MOVE_LIMIT_CHESS(14, "移动限制棋"),
    TURN_ROUND_TAKE_CARDS(15, "夺取手牌"),
    TURN_ROUND_BUILD(16, "建造"),
    TURN_ROUND_TRADE(17, "贸易"),
    TURN_ROUND_TRADE_CONFIRM(18, "贸易确认"),
    TURN_ROUND_TRADE_CANCEL(19, "贸易取消"),
    TURN_ROUND_EXCHANGE_CARDS(20, "兑换发展卡"),
    TURN_ROUND_USED_CARDS(21, "使用发展卡"),
    TURN_ROUND_END(22, "结束操作"),

    LOOK_LOG(100, "查看日志"),
    SEND_MSG(101, "发送会话"),
    FULL_MSG(102, "房间人已满"),
    ;

    private String message;
    private int code;

    ActionTypeEnum(int statusCode, String statusMessage) {
        this.message = statusMessage;
        this.code = statusCode;
    }

    public static ActionTypeEnum getEnum(Integer type) {
        List<ActionTypeEnum> list = Arrays.asList(ActionTypeEnum.values());
        Optional<ActionTypeEnum> optional = list.stream().filter(it -> it.getCode() == type).findFirst();
        return optional.orElse(null);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getCode() {
        return code;
    }
}
