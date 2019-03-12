package com.dcits.comet.commons.exception;

/**
 * 授权异常
 *
 * @author ChengLiang
 */
public class AuthorizationException extends BusinessException {

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String errorCode) {
        super(errorCode);
    }

    public AuthorizationException(String errorCode, Throwable e) {
        super(errorCode, e);
    }

    public AuthorizationException(String errorCode, String... errorMessageParams) {
        super(errorCode, errorMessageParams);
    }

    public AuthorizationException(String errorCode, String[] errorMessageParams, Throwable e) {
        super(errorCode, errorMessageParams, e);
    }
}
