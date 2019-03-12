package com.dcits.comet.commons.business;

/**
 * 服务类型定义
 *
 * @author ChengLiang
 */
public enum ServiceTypeEnum {
    /**
     * 金融服务
     */
    FINANCIAL("1000"),
    /**
     * 非金融服务
     */
    NONFINANCIAL("1200"),
    /**
     * 冲正服务
     */
    REVERSAL("1300"),
    /**
     * 查询服务
     */
    INQUIRY("1400"),
    /**
     * 文件服务
     */
    FILE("1220");

    /**
     * 服务编码
     */
    private String messageType;

    /**
     * 私有构造,防止被外部调用
     *
     * @param messageType
     */
    private ServiceTypeEnum(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }
}
