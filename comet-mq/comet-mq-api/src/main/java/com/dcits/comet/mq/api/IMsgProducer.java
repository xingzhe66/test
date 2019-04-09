package com.dcits.comet.mq.api;

/**
 * @author wangyun
 * @date 2019/4/8
 * @description
 */
public interface IMsgProducer {

    public void send(Message message);

    public void send(String  msgText);

    public void send(String destination,String topic,String  msgText);

}
