package com.dcits.comet.commons.exception;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 * 业务级异常基类
 * 
 */
public class BusinessException extends CometPlatformException {
    private static final long serialVersionUID = -43523452345L;
    private Object extraObject;


    public BusinessException() {
        super();
    }


    public BusinessException(String errorCode) {
        super(errorCode);
    }


    public BusinessException(String errorCode, Throwable e) {
        super(errorCode, e);
    }


    public BusinessException(String errorCode, String... errorMessageParams) {
        super(errorCode, errorMessageParams);
    }


    public BusinessException(String errorCode, String[] errorMessageParams, Throwable e) {
        super(errorCode, errorMessageParams, e);
    }


    public Object getExtraObject() {
        return extraObject;
    }


    public void setExtraObject(Object extraObject) {
        this.extraObject = extraObject;
    }

}
