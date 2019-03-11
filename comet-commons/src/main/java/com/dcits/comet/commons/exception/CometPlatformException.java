package com.dcits.comet.commons.exception;


import com.dcits.comet.commons.constant.Constants.*;

/**
 * 平台级异常基类
 *
 */
public class CometPlatformException extends RuntimeException {
    private static final long serialVersionUID = 123333212566L;

    /** 内部错误码 */
    private String errorCode;

    /** 异常信息 */
    private String errorMessage;


    // extends RuntimeException
    public CometPlatformException() {
        super("");
    }


    // extends RuntimeException
    public CometPlatformException(final Throwable cause) {
        super(cause);
        setErrorCode(ResponseCode.EXCEPTION);

    }


    // extends RuntimeException
    protected CometPlatformException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public CometPlatformException(String errorCode) {
        super(errorCode + ":" + ExceptionContainer.getErrorMessage(errorCode));
        this.errorCode = errorCode;
        this.errorMessage = ExceptionContainer.getErrorMessage(errorCode);
    }


    public CometPlatformException(String errorCode, Throwable e) {
        super(errorCode + ":" + ExceptionContainer.getErrorMessage(errorCode), e);
        this.errorCode = errorCode;
        this.errorMessage = ExceptionContainer.getErrorMessage(errorCode);
    }


    public CometPlatformException(String errorCode, String... errorMessageParams) {
        super(errorCode + ":" + ExceptionContainer.getErrorMessage(errorCode, errorMessageParams));
        this.errorCode = errorCode;
        this.errorMessage = ExceptionContainer.getErrorMessage(errorCode, errorMessageParams);
    }


    public CometPlatformException(String errorCode, String[] errorMessageParams, Throwable e) {
        super(errorCode + ":" + ExceptionContainer.getErrorMessage(errorCode, errorMessageParams), e);
        this.errorCode = errorCode;
        this.errorMessage = ExceptionContainer.getErrorMessage(errorCode, errorMessageParams);
    }


    public String getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    @Override
    public String toString() {
        return String.format("%s :%s:%s", getClass().getName(), errorCode, errorMessage == null ? ""
                : errorMessage);
    }
}
