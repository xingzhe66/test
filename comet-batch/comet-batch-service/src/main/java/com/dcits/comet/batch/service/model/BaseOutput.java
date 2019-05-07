package com.dcits.comet.batch.service.model;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public class BaseOutput {
    private String serviceStatus;
    private String exceptionCode;
    private String exceptionMsg;


    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }
}
