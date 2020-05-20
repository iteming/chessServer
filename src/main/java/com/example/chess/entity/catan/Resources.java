package com.example.chess.entity.catan;

import lombok.Data;

@Data
public class Resources {
    /**
     * 资源类型
     */
    private ResourcesEnum type;
    /**
     * 资源数量
     */
    private int number;
}
