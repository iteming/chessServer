package com.example.chess.common;

import lombok.Data;

/**
 * Created by chanming on 16/7/20.
 */
@Data
public class Result {
    /**
     * 是否成功
     */
    private Boolean code = true;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 返回实体
     */
    private Object data;
}
