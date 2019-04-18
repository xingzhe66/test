package com.dcits.comet.mq.consumer;

import com.dcits.comet.commons.utils.SpringContextUtil;
import com.dcits.comet.mq.api.IMQConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
@Slf4j
public class MQConsumerFactory {
    public static IMQConsumer getInstance(String topic, String tag) {
        String beanName= MQConsumerBeanContainer.getInstance().getBeanName(topic, tag);
        if(null==beanName||"".equals(beanName)){
            log.warn("topic:"+topic+" tag:"+tag+"未设置消费者！");
            return null;
        }
        ApplicationContext appContext = SpringContextUtil.getApplicationContext();

        return appContext.getBean(beanName)==null?null: (IMQConsumer) appContext.getBean(beanName);
    }
}
