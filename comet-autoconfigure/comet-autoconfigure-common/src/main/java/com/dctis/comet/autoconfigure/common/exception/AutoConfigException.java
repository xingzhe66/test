package com.dctis.comet.autoconfigure.common.exception;

/**
 * @ClassName AutoConfigException
 * @Author guanlt
 * @Date 2019/5/21 18:18
 * @Description 自动装载配置异常
 * @Version 1.0
 **/
public class AutoConfigException extends RuntimeException {

    public AutoConfigException(String message) {
        super(message);
    }
}
