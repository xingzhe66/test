package com.dcits.comet.mq.consumer;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.mq.api.IMQConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

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

    private final static Map<String, String> TRANS_CODE_MAP =
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
        init();
    }

    private void init() {
        ApplicationContext appContext = SpringContextUtil.getApplicationContext();
        Map<String, IMQConsumer> map = appContext.getBeansOfType(IMQConsumer.class);
        for(String key:map.keySet()){
            Class clazz=map.get(key).getClass();
            if(clazz.isAnnotationPresent(RocketMQConsumer.class)){
                RocketMQConsumer rocketMQConsumer = (RocketMQConsumer) clazz.getAnnotation(RocketMQConsumer.class);
                TRANS_CODE_MAP.put(rocketMQConsumer.topic()+DL+ rocketMQConsumer.tag(),key);
            }
        }
        log.info("=============================================");
        log.info("=========MQConsumer映射扫描结束！============");
        log.info("=============================================");
    }


    public String getBeanName(String topic, String tag) {
        return TRANS_CODE_MAP.get(topic+DL+tag);
    }
}
