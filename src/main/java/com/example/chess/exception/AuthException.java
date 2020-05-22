package com.example.chess.exception;

import com.example.chess.protocol.ICode;
import com.example.chess.protocol.SysErrorEnum;

/**
 * @author : jiangbin
 * @Date : 2019/7/30 23:38
 * @Description : 权限级异常
 */
public class AuthException extends SysException {

    public AuthException() {
        super(SysErrorEnum.AUTH_ERROR);
    }

    public AuthException(String error) {
        super(SysErrorEnum.AUTH_ERROR.getCode(), error);
    }

    public AuthException(ICode code) {
        super(code);
    }
}
