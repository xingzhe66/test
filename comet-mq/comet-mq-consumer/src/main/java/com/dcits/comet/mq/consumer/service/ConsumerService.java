package com.dcits.comet.mq.consumer.service;

import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.mq.consumer.model.MqConsumerMsgPo;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @ClassName ConsumerService
 * @Author guihj
 * @Date 2019/4/19 17:37
 * @Description TODO
 * @Version 1.0
 **/
@Component
public class ConsumerService {
    @Autowired
    DaoSupport daoSupport;

    /**
     * @Author guihj
     * @Description 保存消费者接收的消息信息
     * @Date 2019/4/21 10:57
     * @Param [messageExt]
     * @return void
     **/
    public  void saveConsumerMessage(MessageExt messageExt){
        MqConsumerMsgPo mqConsumerMsgPo=new MqConsumerMsgPo();
        String id= StringUtil.getUUID();
        mqConsumerMsgPo.setMqMessageId(id);
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
        daoSupport.insert(mqConsumerMsgPo);
    }



    /**
     * @Author guihj
     * @Description （去重）根据消息ID判断该条消息是否已经被接收，如果表中存在，则消息已经被接收，避免二次接收
     * @Date 2019/4/21 10:58
     * @Param
     * @return
     **/
     public boolean isMessageReceived(String msgId){
         boolean result=false;
         MqConsumerMsgPo mqConsumerMsgPo=new MqConsumerMsgPo();
         mqConsumerMsgPo.setMsgId(msgId);
         List<MqConsumerMsgPo> consumerMsgs=daoSupport.selectList(mqConsumerMsgPo);
         if(consumerMsgs!=null && consumerMsgs.size()>0){
             result=true;
         }
         return result;
     }
}
