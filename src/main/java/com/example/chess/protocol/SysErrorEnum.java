package com.example.chess.protocol;

/**
 * @version 1.0.0
 * @author: jiangbin
 * @description 状态码枚举类
 **/
public enum SysErrorEnum implements ICode {

    /***/
    FAILED(-1, "操作失败"),

    SUCCESS(0, "操作成功"),

    /**
     * 权限错误1100 ------1199
     */
    AUTH_ERROR(1100,"安全认证错误"),

    /**
     * 业务错误1200 ------1299
     */
    BIZ_EXCEPTION(1200, "业务错误"),

    /**
     * 系统错误1001 ------1099
     */
    SYS_ERROR(1000,"系统错误"),

    SYS_ILLEGAL_ARGUMENT(1001, "参数错误"),

    SYS_ILLEGAL_ARGUMENT_TYPE(1002, "参数类型错误"),

    SYS_ILLEGAL_DATA(1003, "非法的数据格式"),

    SYS_ILLEGAL_STATE(1004, "非法状态"),

    SYS_MISSING_ARGUMENT(1005, "缺少参数"),

    SYS_METHOD_NOT_ALLOWED(1006, "不支持的方法"),

    SYS_MULTIPART_TOO_LARGE(1007, "文件太大"),

    SYS_MIRE_REQUEST(1008,"系统繁忙,请稍后重试!"),
    ;
    private String message;

    private int code;

    SysErrorEnum(int statusCode, String statusMessage) {
        this.message = statusMessage;
        this.code = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getCode() {
        return code;
    }

}
