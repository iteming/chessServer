package com.example.chess.entity.catan;

import com.example.chess.entity.catan.constant.ChessEntityEnum;
import com.example.chess.entity.catan.constant.KeyValueMap;
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
     * 节点 0 的时候，记录所属<棋牌id = id, 节点id = value>
     * type == 1 || type == 3
     */
    private KeyValueMap nodePosition;

    /**
     * 道路 1 的时候，记录所属<棋牌id = id, 节点起 = key，节点止 = value>
     * type == 2
     */
    private KeyValueMap roadPosition;
}
