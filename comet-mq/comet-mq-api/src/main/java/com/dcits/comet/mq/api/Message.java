package com.dcits.comet.mq.api;

/**
 * @Author wangyun
 * @Date 2019/4/8
 **/
public class Message {

    private String topic;

    private String msgText;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
}
