package com.dcits.comet.mq.api;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
public interface IMQConsumer {
    void onMessage(Message message);

}
