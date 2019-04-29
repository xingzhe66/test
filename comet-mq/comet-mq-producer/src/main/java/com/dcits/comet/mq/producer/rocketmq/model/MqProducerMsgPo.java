package com.dcits.comet.mq.producer.rocketmq.model;

import com.dcits.comet.dao.annotation.TablePk;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author guihj
 * @Description MQ生产者信息消息表(UPRIGHT)
 * @Date 2019-04-19 16:40:04
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper=false)
@TableType(name="MQ_PRODUCER_MSG",value=TableTypeEnum.UPRIGHT)
public class MqProducerMsgPo extends BasePo {

	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.mq_msg_id
	* @Description  消息id
	*/
	@TablePk(index=1)
	private String mqMsgId;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.flow_id
	* @Description  流程id
	*/
	private String flowId;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.brokerName
	* @Description  broker名称
	*/
	private String brokerName;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.offset_msg_id
	* @Description  消息发送成功，broker生成id
	*/
	private String offsetMsgId;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.msg_id
	* @Description  消息发送成功，producer生成id
	*/
	private String msgId;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.message
	* @Description  消息内容
	*/
	private String message;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.create_time
	* @Description  创建时间
	*/
	private String createTime;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.last_update
	* @Description  最后一次更新时间
	*/
	private String lastUpdate;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.status
	* @Description  状态:1-消息建立；2-待发送；3-发送成功；4-异常
	*/
	private Integer status;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.message_type
	* @Description  消息类型
	*/
	private String messageType;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.seq_no
	* @Description  消息序列号
	*/
	private Integer seqNo;
	/**
	* This field corresponds to the database column MQ_PRODUCER_MSG.queue_id
	* @Description  
	*/
	private Integer queueId;
}
