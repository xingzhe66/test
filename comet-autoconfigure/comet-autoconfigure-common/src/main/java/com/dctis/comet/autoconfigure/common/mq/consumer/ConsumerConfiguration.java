package com.dctis.comet.autoconfigure.common.mq.consumer;


import com.dcits.comet.mq.consumer.MQConsumeListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;


/**
 * @author wangyun
 * @date 2019/4/18
 * @description
 */
@SpringBootConfiguration
@ConditionalOnProperty(name = "rocketmq.isEnable", havingValue = "true",matchIfMissing = false)
public class ConsumerConfiguration {
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
    @Autowired
    private MQConsumeListener mQConsumeListener;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumerOne() {
        if (StringUtils.isEmpty(groupName)) {
            // throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL,"groupName is null !!!",false);
        }
        if (StringUtils.isEmpty(namesrvAddr)) {
            //  throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL,"namesrvAddr is null !!!",false);
        }
//        if (StringUtils.isEmpty(topics)) {
//            //  throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL,"topics is null !!!",false);
//        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(mQConsumeListener);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        /**
         * 设置一次消费消息的条数，默认为1条
         */
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
//        try {
//            /**
//             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
//             */
//
////            consumer.start();
//       //     LOGGER.info("consumer is init !!! groupName:{},topics:{},namesrvAddr:{}", groupName, topics, namesrvAddr);
//        } catch (MQClientException e) {
//       //     LOGGER.error("consumer is init !!! groupName:{},topics:{},namesrvAddr:{}", groupName, topics, namesrvAddr, e);
//            throw new MQException(e);
//        }
        return consumer;
    }
}
