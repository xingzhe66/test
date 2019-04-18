package com.dcits.comet.mq.api;



import javax.print.attribute.standard.Destination;

/**
 * @author wangyun
 * @date 2019/4/8
 * @description
 */

public interface IMsgProducer {


    /**
     * @Author guihj
     * @Description 指定消息的 主题，标签 和消息内容
     * @Date 2019/4/10 15:08
     * @Param [topic, tags, msg]
     * @return void
     **/
    public void sendMessage(String topic,String tags,String messageTxt);


    /**
     * @Author guihj
     * @Description 指定消息的 主题，消息内容
     * @Date 2019/4/10 15:08
     * @Param [topic, tags, msg]
     * @return void
     **/
    public void sendMessage(String topic,String messageTxt);


    /**
     * @Author guihj
     * @Description 消息
     * @Date 2019/4/10 15:08
     * @Param [topic, tags, msg]
     * @return void
     **/
    public void sendMessage(RocketMessage message);


}
