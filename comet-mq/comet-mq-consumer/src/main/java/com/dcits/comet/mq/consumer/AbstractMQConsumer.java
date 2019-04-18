package com.dcits.comet.mq.consumer;

import com.dcits.comet.mq.api.IMQConsumer;
import com.dcits.comet.mq.api.Message;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
public abstract class AbstractMQConsumer implements IMQConsumer {

    @Override
    public abstract void onMessage(Message message);


}
