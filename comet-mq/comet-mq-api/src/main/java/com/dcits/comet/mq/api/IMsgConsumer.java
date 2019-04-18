package com.dcits.comet.mq.api;

/**
 * @Author wangyun
 * @Date 2019/4/8
 **/
public interface IMsgConsumer {

    public RocketMessage receive();

}
