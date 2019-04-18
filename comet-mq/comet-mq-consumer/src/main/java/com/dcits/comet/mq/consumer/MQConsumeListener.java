package com.dcits.comet.mq.consumer;

import com.dcits.comet.mq.api.IMQConsumer;
import com.dcits.comet.mq.api.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
@Component
@Slf4j
public class MQConsumeListener implements MessageListenerConcurrently {

    /**
     *  默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
     *  不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(msgs)){
            log.info("接受到的消息为空，不处理，直接返回成功");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        //默认每次接收一条
        MessageExt messageExt = msgs.get(0);
        log.info("接受到的消息为："+new String(messageExt.getBody()));
        IMQConsumer mqConsumer=MQConsumerFactory.getInstance(messageExt.getTopic(),messageExt.getTags());
        if(mqConsumer!=null){
            //TODO 判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）

            Message message=new Message();
            message.setMsgText(new String(messageExt.getBody()));
            //处理对应的业务逻辑
            mqConsumer.onMessage(message);
        }
        if(mqConsumer==null){
            int reconsume = messageExt.getReconsumeTimes();
            if(reconsume ==3){//消息已经重试了3次，如果不需要再次消费，则返回成功
                //todo 没有实现消费接口而又监听了这个topic的tag，可能是配置错误，需要先持久化到本地；
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
