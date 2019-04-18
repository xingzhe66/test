package com.dcits.comet.mq.consumer.test;

import com.dcits.comet.mq.consumer.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
@Slf4j
public class ProducerTest {
    public static void main(String[] arg){
        DefaultMQProducer producer;
        producer = new DefaultMQProducer("wangyun");

        producer.setNamesrvAddr("10.7.25.201:9876");
        // producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");

        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //producer.setInstanceName(instanceName);
//        if(this.maxMessageSize!=null){
//            producer.setMaxMessageSize(this.maxMessageSize);
//        }
//        if(this.sendMsgTimeout!=null){
//            producer.setSendMsgTimeout(this.sendMsgTimeout);
//        }
//        //如果发送消息失败，设置重试次数，默认为2次
//        if(this.retryTimesWhenSendFailed!=null){
//            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
//        }

        try {
            producer.start();

            log.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , "wangyun","10.7.25.201:9876"));
        } catch (MQClientException e) {
            log.error(String.format("producer is error {}"
                    , e.getMessage(),e));
            throw new MQException(e);
        }

        Message sendMsg = new Message("dcits","dc","haha".getBytes());
        //默认3秒超时
        try {
            SendResult sendResult = producer.send(sendMsg);

        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
