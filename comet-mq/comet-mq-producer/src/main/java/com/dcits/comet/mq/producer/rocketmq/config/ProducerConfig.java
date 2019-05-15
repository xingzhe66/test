package com.dcits.comet.mq.producer.rocketmq.config;
import com.dcits.comet.mq.producer.rocketmq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @ClassName ProducerConfig
 * @Author guihj
 * @Date 2019/4/10 15:26
 * @Description TODO
 * @Version 1.0
 **/

@Slf4j
@Configuration
@EnableConfigurationProperties({ProducerConfigProperties.class})
@ConditionalOnProperty(name = "rocketmq.isEnable", havingValue = "true",matchIfMissing = false)
public class ProducerConfig {

    @Bean
    public DefaultMQProducer getRocketMQProducer(final ProducerConfigProperties producerConfigProperties) throws MQException {
        //if(StringUtils.isEmpty(this.groupName)){
        if(StringUtils.isEmpty(producerConfigProperties.getGroupName())){
            throw new MQException("消息生产者groupName不能为空");
        }

        if (StringUtils.isEmpty(producerConfigProperties.getNamesrvAddr())) {
            throw new MQException("消息生产者IP和端口号不能为空");
        }

        DefaultMQProducer producer;
        producer = new DefaultMQProducer(producerConfigProperties.getGroupName());

        producer.setNamesrvAddr(producerConfigProperties.getNamesrvAddr());
       // producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");

        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //producer.setInstanceName(instanceName);
        if(producerConfigProperties.getMaxMessageSize()!=null){
            producer.setMaxMessageSize(producerConfigProperties.getMaxMessageSize());
        }
        if(producerConfigProperties.getSendMsgTimeout()!=null){
            producer.setSendMsgTimeout(producerConfigProperties.getSendMsgTimeout());
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if(producerConfigProperties.getRetryTimesWhenSendFailed()!=null){
            producer.setRetryTimesWhenSendFailed(producerConfigProperties.getRetryTimesWhenSendFailed());
        }
        try {
            producer.start();
            log.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , producerConfigProperties.getGroupName(), producerConfigProperties.getNamesrvAddr()));
        } catch (MQClientException e) {
            log.error(String.format("producer is error {}"
                    , e.getMessage(),e));
            throw new MQException(e);
        }
        return producer;

    }
}
