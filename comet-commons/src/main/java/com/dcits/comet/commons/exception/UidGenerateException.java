package com.dcits.comet.commons.exception;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/3/27 9:47
 * @see UidGenerateException
 **/
public class UidGenerateException extends BusinessException {
    public UidGenerateException() {
        super();
    }

    public UidGenerateException(String errorCode) {
        super(errorCode);
    }

    public UidGenerateException(String errorCode, Throwable e) {
        super(errorCode, e);
    }

    public UidGenerateException(String errorCode, String... errorMessageParams) {
        super(errorCode, errorMessageParams);
    }

    public UidGenerateException(String errorCode, String[] errorMessageParams, Throwable e) {
        super(errorCode, errorMessageParams, e);
    }
}
