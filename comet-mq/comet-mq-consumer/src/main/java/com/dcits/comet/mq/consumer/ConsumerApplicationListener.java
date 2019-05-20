package com.dcits.comet.mq.consumer;

import com.dcits.comet.mq.consumer.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName ProducerConfig
 * @Author guihj
 * @Date 2019/4/10 15:26
 * @Description TODO
 * @Version 1.0
 **/

@Slf4j
@Component
public class ConsumerApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DefaultMQPushConsumer defaultMQPushConsumer;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //初始化容器
        MQConsumerBeanContainer.initContainer();
        Map consumerBeanMap =MQConsumerBeanContainer.getConsumerBeanMap();
        //启动consume服务监听
        try {
          //  String[] topicTagsArr = topics.split(";");
            Set<String> keySet=consumerBeanMap.keySet();
            for (String topicTags : keySet) {
                String[] topicTag = topicTags.split("\\|");
                log.info("topic:"+topicTag[0]+",tag:"+topicTag[1]);
                defaultMQPushConsumer.subscribe(topicTag[0], topicTag[1]);
            }
            defaultMQPushConsumer.start();
            log.info("consumer is start !!! groupName:{}", defaultMQPushConsumer.getConsumerGroup());
        } catch (MQClientException e) {
            log.error("consumer is start !!! groupName:{}", defaultMQPushConsumer.getConsumerGroup());
            throw new MQException(e);
        }
    }
}
