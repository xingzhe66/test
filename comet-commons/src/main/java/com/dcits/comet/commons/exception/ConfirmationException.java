package com.dcits.comet.commons.exception;

public class ConfirmationException extends BusinessException {

    public ConfirmationException() {
        super();
    }

    public ConfirmationException(String errorCode) {
        super(errorCode);
    }

    public ConfirmationException(String errorCode, Throwable e) {
        super(errorCode, e);
    }

    public ConfirmationException(String errorCode, String... errorMessageParams) {
        super(errorCode, errorMessageParams);
    }

    public ConfirmationException(String errorCode, String[] errorMessageParams, Throwable e) {
        super(errorCode, errorMessageParams, e);
    }

}
