package com.dcits.comet.mq.consumer.model;

import com.dcits.comet.dao.annotation.TablePk;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author guihj
 * @Description MQ消费者信息消息表(UPRIGHT)
 * @Date 2019-04-19 16:40:04
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper=false)
@TableType(name="MQ_CONSUMER_MSG",value=TableTypeEnum.UPRIGHT)
public class MqConsumerMsgPo extends BasePo {

	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.mq_message_id
	* @Description  消费者消息主键
	*/
	@TablePk(index=1)
	private String mqMessageId;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.msg_id
	* @Description  接收消息id,与生产者id一致
	*/
	private String msgId;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.born_time
	* @Description  生产时间
	*/
	private String bornTime;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.born_host
	* @Description  生产者host
	*/
	private String bornHost;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.store_time
	* @Description  存储在broker时间
	*/
	private String storeTime;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.store_host
	* @Description  broker的host
	*/
	private String storeHost;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.topic
	* @Description  消息主题
	*/
	private String topic;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.tag
	* @Description  消息标签
	*/
	private String tag;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.queue_id
	* @Description  队列id
	*/
	private Integer queueId;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.recive_time
	* @Description  生产者接收时间
	*/
	private String receiveTime;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.status
	* @Description  消息状态：1-接收成功，3:消费成功，4:消费失败
	*/
	private Integer status;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.update_time
	* @Description  最后一次更新状态时间
	*/
	private String updateTime;
	/**
	* This field corresponds to the database column MQ_CONSUMER_MSG.remark
	* @Description  备注
	*/
	private String remark;
}
