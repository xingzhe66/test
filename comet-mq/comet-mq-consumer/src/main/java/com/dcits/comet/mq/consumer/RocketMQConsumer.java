package com.dcits.comet.mq.consumer;

import java.lang.annotation.*;

/**
 * @author wangyun
 * @date 2019/4/18
 * @description
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQConsumer {
    String topic();
    String tag();

}
