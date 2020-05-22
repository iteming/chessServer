package com.example.chess.cache;

import lombok.Data;

import java.io.Serializable;

@Data
public class CacheEntity<T> implements Serializable {

    enum DataType {
        String, List, Hash, Set;
    }

    /**
     * key
     */
    private String key;

    /**
     * 值
     */
    private T value;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     *
     */
    private DataType type;
    /**
     * 有效时间
     */
    private Long validTime = 0L;


    public CacheEntity(String key, T value, Long validTime) {
        this.key = key;
        this.value = value;
        this.validTime = validTime;
        this.startTime = System.currentTimeMillis();
        this.type = DataType.String;
    }

    public CacheEntity(String key, T value, Long validTime, DataType type) {
        this.key = key;
        this.value = value;
        this.validTime = validTime;
        this.startTime = System.currentTimeMillis();
        this.type = type;
    }

}
