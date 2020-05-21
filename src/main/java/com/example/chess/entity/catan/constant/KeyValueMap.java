package com.example.chess.entity.catan.constant;

import lombok.Data;

@Data
public class KeyValueMap {
    /**
     * 节点ID
     */
    private Integer id;

    private PropertyEnum property;

    private Integer key;

    private Integer value;

    public KeyValueMap(){ }
    public KeyValueMap(Integer id, PropertyEnum property){
        this.id = id;
        this.property = property;
        if (property == PropertyEnum.ANY_THING){
            this.value = 3;
        } else {
            this.value = 2;
        }
    }
}
