package com.example.chess.entity.catan;

import com.example.chess.entity.catan.constant.KeyValueMap;
import com.example.chess.entity.catan.constant.PropertyEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChessBoard {

    /**
     * 唯一主键id
     */
    private Integer id;

    /**
     * 属性：沙漠、水、树、砖、羊、麦子、石头
     */
    private PropertyEnum property;

    /**
     * 数字号码
     */
    private Integer number;

    /**
     * 节点 通常都有6个节点 <节点序号 = id ，<节点兑换属性 = property，兑换所需数量 = value>>
     */
    private List<KeyValueMap> node;

    ChessBoard(Integer id, PropertyEnum property, Integer number, List<KeyValueMap> node) {
        this.id = id;
        this.property = property;
        this.number = number;
        this.node = node;
    }

    /**
     * 初始化节点
     * special 特殊节点，记录 <节点兑换属性，兑换所需数量，节点id 1，节点id 2>
     */
    public static List<KeyValueMap> initNode(List<KeyValueMap> listNode) {
        List<KeyValueMap> list = new ArrayList<>();
        KeyValueMap keyValueMap;
        for (int i = 0; i < 6; i++) {
            keyValueMap = new KeyValueMap();
            keyValueMap.setId(i);
            for (KeyValueMap node : listNode) {
                if (node.getId() == i) {
                    keyValueMap.setProperty(node.getProperty());
                    keyValueMap.setValue(node.getValue());
                }
            }
            list.add(keyValueMap);
        }
        return list;
    }
}
