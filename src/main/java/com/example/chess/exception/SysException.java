package com.example.chess.exception;


import com.example.chess.protocol.ICode;
import com.example.chess.protocol.SysErrorEnum;

/**
 * @author : jiangbin
 * @Date : 2019/7/30 23:38
 * @Description : 系统级异常
 */
public class SysException extends RuntimeException implements ICode {

    protected int code;

    protected String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SysException() {
        this.code = SysErrorEnum.SYS_ERROR.getCode();
        this.message = SysErrorEnum.SYS_ERROR.getMessage();
    }

    public SysException(String error, Throwable cause) {
        super(error, cause);
        this.code = SysErrorEnum.SYS_ERROR.getCode();
        this.message = error;
    }

    public SysException(String error) {
        this.code = SysErrorEnum.SYS_ERROR.getCode();
        this.message = error;
    }

    public SysException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public SysException(ICode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }
}
