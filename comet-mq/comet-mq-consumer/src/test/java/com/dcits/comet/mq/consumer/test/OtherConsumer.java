package com.dcits.comet.mq.consumer.test;

import com.dcits.comet.mq.api.Message;
import com.dcits.comet.mq.consumer.AbstractMQConsumer;
import com.dcits.comet.mq.consumer.RocketMQConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
@Component
@RocketMQConsumer(topic="dcits",tag="dc")
@Slf4j
public class OtherConsumer extends AbstractMQConsumer {
    @Override
    public void onMessage(Message message) {
        log.info("OnMessage : "+message.toString());
    }
}
