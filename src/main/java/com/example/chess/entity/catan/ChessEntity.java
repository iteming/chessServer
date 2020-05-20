package com.example.chess.entity.catan;

import lombok.Data;

@Data
public class ChessEntity {

    /**
     * 资源类型(
     *     VILLAGE(1, "村庄"),
     *     ROAD(2, "道路"),
     *     CITY(3, "城市"),
     *     CARDS(4, "发展卡")
     *     )
     */
    private ChessEntityEnum type;

    /**
     * 位置 (节点 0，路 1)
     */
    private int posType;
    private int pos1;
    private int pos2;
    private int pos3;
}
