package com.dcits.comet.commons.business;

public enum MessageTypeEnum {
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

    private String messageType;

    MessageTypeEnum(String messageType){
        this.messageType=messageType;
    }

    public String getMessageType() {
        return messageType;
    }

//    public void setMessageType(String messageType) {
//        this.messageType = messageType;
//    }


}
