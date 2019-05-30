package com.dctis.comet.autoconfigure.common.mq.producer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/8 13:02
 **/
@Data
@ConfigurationProperties(prefix = ProducerConfigProperties.PREFIX, ignoreUnknownFields = true)
public class ProducerConfigProperties {
    public static final String PREFIX = "rocketmq.producer";

    private Boolean isOnOff;

    private Boolean sofaEnable;
    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    private String groupName;

    private String namesrvAddr;
    /**
     * 消息最大大小，默认4M
     */
    private Integer maxMessageSize = 1024 * 4;
    /**
     * 消息发送超时时间，默认3秒
     */
    private Integer sendMsgTimeout = 3;
    /**
     * 消息发送失败重试次数，默认2次
     */
    private Integer retryTimesWhenSendFailed = 2;

}
