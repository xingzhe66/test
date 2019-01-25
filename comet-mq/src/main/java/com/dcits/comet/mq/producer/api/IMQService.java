package com.dcits.comet.mq.producer.api;

import com.dcits.comet.mq.exception.CometMQException;

public interface IMQService {

    public void send(String topic, String tags,String msg) throws CometMQException;

}
