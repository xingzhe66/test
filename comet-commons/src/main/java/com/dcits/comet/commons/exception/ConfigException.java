package com.dcits.comet.commons.exception;

import java.io.IOException;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public class ConfigException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
