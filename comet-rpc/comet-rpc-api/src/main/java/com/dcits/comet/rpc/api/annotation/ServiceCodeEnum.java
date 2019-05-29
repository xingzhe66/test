package com.dcits.comet.rpc.api.annotation;

/**
 * 服务类型定义
 *
 * @author ChengLiang
 */
public enum ServiceCodeEnum {
    MBSDCODE("MbsdCore"),
    MBSDPRIICE("MbsdPrice"),
    MBSDACCOUNT("MbsdAccount");

    /**
     * 服务编码
     */
    private String serviceCode;

    /**
     * 私有构造,防止被外部调用
     *
     * @param serviceCode
     */
    private ServiceCodeEnum(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }


}

