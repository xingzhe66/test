package com.dcits.comet.rpc.api.constants;

/**
 * 返回码定义
 *
 * @author ChengLiang
 */
public enum ResponseEnum {

    /**
     * 执行成功
     */
    SUCCESS("000000", "SUCCESS"),
    /**
     * 执行失败时
     */
    FAILED("999999", "业务执行异常!");


    //自定义异常码
    private String code;
    //异常信息说明
    private String message;

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResponseEnum{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
