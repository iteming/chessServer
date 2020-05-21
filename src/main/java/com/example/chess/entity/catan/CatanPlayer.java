package com.example.chess.entity.catan;

import com.example.chess.entity.catan.constant.UserStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class CatanPlayer {
    /**
     * 本轮是否已经执行过了
     */
    private Boolean isDone;
    /**
     * 用户状态
     */
    private UserStatusEnum status;
    /**
     * 棋牌颜色 红 red 黄 yellow 绿 green
     */
    private String color;

    /**
     * 序号
     */
    private int number;

    /**
     * 本局已经使用发展卡
     */
    private int roundUsedDevelopCards;
    /**
     * 本局已创建最长道路
     */
    private int roundBuildLongRoad;
    /**
     * 本局已获得的胜利点
     */
    private int roundPoints;

    /**
     * 棋牌集合（村庄、道路、城市、发展卡数量）
     */
    private List<ChessEntity> chessEntity;

    /**
     * 资源集合（五种资源）
     */
    private List<Resources> resources;
}
