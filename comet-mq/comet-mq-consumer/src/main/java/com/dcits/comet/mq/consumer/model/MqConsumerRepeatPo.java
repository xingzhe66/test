package com.dcits.comet.mq.consumer.model;

import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author guihj
 * @Description MQ消费者信息消息表(UPRIGHT)
 * @Date 2019-05-10 16:42:57
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper=false)
@TableType(name="MQ_CONSUMER_REPEAT",value=TableTypeEnum.UPRIGHT)
public class MqConsumerRepeatPo extends BasePo {

    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.mq_message_id
     * @Description  消费者消息主键
     */

    private String mqMessageId;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.msg_id
     * @Description  接收消息id,与生产者id一致
     */
    private String msgId;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.born_time
     * @Description  生产时间
     */
    private String bornTime;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.born_host
     * @Description  生产者host
     */
    private String bornHost;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.store_time
     * @Description  存储在broker时间
     */
    private String storeTime;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.store_host
     * @Description  broker的host
     */
    private String storeHost;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.topic
     * @Description  消息主题
     */
    private String topic;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.tag
     * @Description  消息标签
     */
    private String tag;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.queue_id
     * @Description  队列id
     */
    private Integer queueId;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.receive_time
     * @Description  生产者接收时间
     */
    private String receiveTime;
    /**
     * This field corresponds to the database column MQ_CONSUMER_REPEAT.remark
     * @Description  备注
     */
    private String remark;
}
