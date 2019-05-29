package com.dcits.comet.mq.consumer;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.mq.api.IMQConsumer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
@Slf4j
public class MQConsumerBeanContainer {
    public static final String DL = "|";
    private static volatile MQConsumerBeanContainer mqConsumerBeanContainer;

    private final static Map<String, String> CONSUMER_BEAN_MAP =
            new HashMap<String, String>();

    public static MQConsumerBeanContainer getInstance() {
        if (mqConsumerBeanContainer == null) {
            synchronized (MQConsumerBeanContainer.class) {
                if (mqConsumerBeanContainer == null) {
                    mqConsumerBeanContainer = new MQConsumerBeanContainer();
                }
            }
        }
        return mqConsumerBeanContainer;
    }

    private MQConsumerBeanContainer() {
    }

    public void initContainer() {
        //TODO 加载顺序问题
        Map<String, IMQConsumer> map = SpringContextUtil.getBeansOfType(IMQConsumer.class);
        for (String key : map.keySet()) {
            Class clazz = map.get(key).getClass();
            if (clazz.isAnnotationPresent(RocketMQConsumer.class)) {
                RocketMQConsumer rocketMQConsumer = (RocketMQConsumer) clazz.getAnnotation(RocketMQConsumer.class);
                CONSUMER_BEAN_MAP.put(rocketMQConsumer.topic() + DL + rocketMQConsumer.tag(), key);
            }
        }
        log.info("=============================================");
        log.info("=========MQConsumer映射扫描结束！============");
        log.info("=============================================");
    }

    public String getBeanName(String topic, String tag) {
        return CONSUMER_BEAN_MAP.get(topic + DL + tag);
    }

    public Map getConsumerBeanMap() {
        return CONSUMER_BEAN_MAP;
    }
}
