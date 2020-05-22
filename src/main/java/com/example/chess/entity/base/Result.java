package com.example.chess.entity.base;

import com.example.chess.protocol.ICode;
import com.example.chess.protocol.SysErrorEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 江斌  <br/>
 * @version 1.0  <br/>
 * @ClassName: Result <br/>
 * @date: 2019/9/23 11:31 <br/>
 * @Description: 返回的数据模型 <br/>
 * @since JDK 1.8
 */
@Data
public class Result<T> implements Serializable {
    /**
     * 状态值
     */
    private int status;
    /**
     * 状态翻译
     */
    private String message;
    /**
     * 返回对象
     */
    private T data;

    public Result() {

    }

    public Result(ICode code) {
        this.status = code.getCode();
        this.message = code.getMessage();
    }

    public Result(ICode code, T data) {
        this.status = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Result(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isSucceed() {
        return SysErrorEnum.SUCCESS.getCode() == status;
    }

    /**
     * 操作成功
     */
    public static Result success() {
        return new Result(SysErrorEnum.SUCCESS);
    }

    /**
     * 操作成功
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SysErrorEnum.SUCCESS, data);
    }

    /**
     * 操作失败
     */
    public static Result failed() {
        return new Result(SysErrorEnum.FAILED);
    }

    /**
     * 操作失败
     */
    public static <T> Result<T> failed(T data) {
        return new Result<>(SysErrorEnum.FAILED, data);
    }

    /**
     * 自定义返回
     */
    public static Result custom(ICode code) {
        return custom(code, null);
    }

    /**
     * 自定义返回
     */
    public static Result custom(int status, String message) {
        return new Result(status, message);
    }

    /**
     * 自定义返回
     */
    public static <T> Result<T> custom(int status, String message, T data) {
        return new Result<>(status, message, data);
    }

    /**
     * 自定义返回
     */
    public static <T> Result<T> custom(ICode code, T data) {
        return new Result<>(code, data);
    }
}

