package com.dcits.comet.mq.consumer.service;

import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.mq.consumer.model.MqConsumerMsgPo;
import com.dcits.comet.mq.consumer.model.MqConsumerRepeatPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @ClassName ConsumerService
 * @Author guihj
 * @Date 2019/4/19 17:37
 * @Description TODO
 * @Version 1.0
 **/
@Slf4j
@Component
public class ConsumerService {
    @Autowired
    DaoSupport daoSupport;

    /**
     * @Author guihj
     * @Description 保存消费者接收的消息信息，如果存入失败，则说明已经存在。将该消息存入Mq_Consumer_Repeat表
     * @Date 2019/4/21 10:57
     * @Param [messageExt]
     * @return void
     **/
    public  boolean saveConsumerMessage(MessageExt messageExt){
        boolean flag=false;
        MqConsumerMsgPo mqConsumerMsgPo=new MqConsumerMsgPo();
        String keys=messageExt.getKeys();
        mqConsumerMsgPo.setMqMessageId(keys);
        mqConsumerMsgPo.setMsgId(messageExt.getMsgId());
        mqConsumerMsgPo.setBornTime(DateUtil.stampToDate(messageExt.getBornTimestamp()));
        mqConsumerMsgPo.setBornHost(messageExt.getBornHostNameString());
        mqConsumerMsgPo.setStoreTime(DateUtil.stampToDate(messageExt.getStoreTimestamp()));
        mqConsumerMsgPo.setStoreHost(messageExt.getStoreHost().toString());
        mqConsumerMsgPo.setTag(messageExt.getTags());
        mqConsumerMsgPo.setTopic(messageExt.getTopic());
        mqConsumerMsgPo.setQueueId(messageExt.getQueueId());
        mqConsumerMsgPo.setReceiveTime(DateUtil.getCurrentDate());
        mqConsumerMsgPo.setStatus(1);
        try {
            daoSupport.insert(mqConsumerMsgPo);
            flag=true;
        } catch (Exception e) {
            MqConsumerRepeatPo mqConsumerRepeatPo=new MqConsumerRepeatPo();
            if(e instanceof DuplicateKeyException || e.getMessage().indexOf("ORA-00001") != -1 ){
                log.warn(messageExt.getMsgId()+"消息已经存在于MQ_CONSUMER_MSG表中，无需重复插入");
                mqConsumerRepeatPo.setRemark("消息重复");
            } else {
                log.info(messageExt.getMsgId()+"消息插入失败");
                mqConsumerRepeatPo.setRemark("消息插入失败");
            }
            mqConsumerRepeatPo.setMqMessageId(mqConsumerMsgPo.getMqMessageId());
            mqConsumerRepeatPo.setMsgId(mqConsumerMsgPo.getMsgId());
            mqConsumerRepeatPo.setBornHost(mqConsumerMsgPo.getBornHost());
            mqConsumerRepeatPo.setBornTime(mqConsumerMsgPo.getBornTime());
            mqConsumerRepeatPo.setQueueId(mqConsumerMsgPo.getQueueId());
            mqConsumerRepeatPo.setReceiveTime(mqConsumerMsgPo.getReceiveTime());
            mqConsumerRepeatPo.setStoreHost(mqConsumerMsgPo.getStoreHost());
            mqConsumerRepeatPo.setStoreTime(mqConsumerMsgPo.getStoreTime());
            mqConsumerRepeatPo.setTag(mqConsumerMsgPo.getTag());
            mqConsumerRepeatPo.setTopic(mqConsumerMsgPo.getTopic());
            mqConsumerMsgPo.setReceiveTime(DateUtil.getCurrentDate());
            daoSupport.insert(mqConsumerRepeatPo);
        }
        return flag;
    }
    /**
     * @Author guihj
     * @Description （去重）根据消息ID判断该条消息是否已经被接收，如果表中存在，则消息已经被接收，避免二次接收
     * @Date 2019/4/21 10:58
     * @Param
     * @return
     **/
    public boolean isMessageReceived(String messageId){
        boolean result=false;
        MqConsumerMsgPo mqConsumerMsgPo=new MqConsumerMsgPo();
        mqConsumerMsgPo.setMqMessageId(messageId);
        List<MqConsumerMsgPo> consumerMsgs=daoSupport.selectList(mqConsumerMsgPo);
        if(consumerMsgs!=null && consumerMsgs.size()>0){
            result=true;
        }
        return result;
    }


    public void saveRepeatConsumerMsg(MessageExt messageExt){
        MqConsumerRepeatPo mqConsumerRepeatPo=new MqConsumerRepeatPo();
        mqConsumerRepeatPo.setMqMessageId(messageExt.getKeys());
        mqConsumerRepeatPo.setMsgId(messageExt.getMsgId());
        mqConsumerRepeatPo.setBornTime(DateUtil.stampToDate(messageExt.getBornTimestamp()));
        mqConsumerRepeatPo.setBornHost(messageExt.getBornHostNameString());
        mqConsumerRepeatPo.setStoreTime(DateUtil.stampToDate(messageExt.getStoreTimestamp()));
        mqConsumerRepeatPo.setStoreHost(messageExt.getStoreHost().toString());
        mqConsumerRepeatPo.setTag(messageExt.getTags());
        mqConsumerRepeatPo.setTopic(messageExt.getTopic());
        mqConsumerRepeatPo.setQueueId(messageExt.getQueueId());
        mqConsumerRepeatPo.setReceiveTime(DateUtil.getCurrentDate());
        mqConsumerRepeatPo.setRemark("消息重复");
        daoSupport.insert(mqConsumerRepeatPo);
    }

    /**
     * @Author guihj
     * @Description //topic 或者 tag 不存在时，修改消息状态
     * @Date 2019/5/15 15:00
     * @Param [topic, tag]
     * @return void
     **/
    public  void updateConsumerMsg(String messageId,String topic,String tag ){
        MqConsumerMsgPo mqConsumerMsgPo=new MqConsumerMsgPo();
        mqConsumerMsgPo.setUpdateTime(DateUtil.getCurrentDate());
        mqConsumerMsgPo.setStatus(2);
        mqConsumerMsgPo.setRemark("topic"+topic+",tag"+tag+"未设置消费者！");
        mqConsumerMsgPo.setMqMessageId(messageId);
        daoSupport.update(mqConsumerMsgPo);
    }




}
