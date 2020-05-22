package com.example.chess.exception;


import com.example.chess.protocol.ICode;
import com.example.chess.protocol.SysErrorEnum;

/**
 * @author : jiangbin
 * @Date : 2019/7/30 23:38
 * @Description : 业务级异常
 */
public class BizException extends SysException {

    public BizException() {
        super(SysErrorEnum.BIZ_EXCEPTION);
    }

    public BizException(String error) {
        super(SysErrorEnum.BIZ_EXCEPTION.getCode(), error);
    }

    public BizException(ICode code) {
        super(code);
    }

}
