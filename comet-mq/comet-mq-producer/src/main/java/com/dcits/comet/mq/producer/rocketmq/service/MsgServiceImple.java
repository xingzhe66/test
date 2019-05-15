package com.dcits.comet.mq.producer.rocketmq.service;

import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.mq.api.IMsgService;
import com.dcits.comet.mq.api.RocketMessage;
import com.dcits.comet.mq.producer.rocketmq.contanst.MessageStatus;
import com.dcits.comet.mq.producer.rocketmq.model.MqProducerMsgPo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MsgServiceImple
 * @Author guihj
 * @Date 2019/4/11 22:06
 * @Description TODO
 * @Version 1.0
 **/
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.isEnable", havingValue = "true",matchIfMissing = false)
@Transactional(transactionManager = "shardTransactionManager",propagation = Propagation.REQUIRES_NEW)
public class MsgServiceImple implements IMsgService {

    @Autowired
    MessageService messageService;

    @Autowired
    DaoSupport daoSupport;
    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    DefaultMQProducer defaultMQProducer;

    @Autowired
    Gson gson;

    /**
     * @Author guihj
     * @Description //使用rocketMQ生产者真实发送消息
     * @Date 2019/4/15 15:04
     * @Param [rocketMessage]
     * @return void
     **/
    @Override
    public  SendResult  realSend(RocketMessage rocketMessage) throws Exception {
        Message sendMsg = new Message(
                rocketMessage.getTopic(), rocketMessage.getTag(),rocketMessage.getMessageId(),
                rocketMessage.getMsgText().getBytes(RemotingHelper.DEFAULT_CHARSET));
        return defaultMQProducer.send(sendMsg);
    }

    /**
     * @Author guihj
     * @Description 根据flowId查询所有消息集合
     * @Date 2019/4/15 14:55
     * @Param []
     * @return java.util.List<com.dcits.comet.mq.api.RocketMessage>
     **/
    @Override
    public List<RocketMessage>  getMessageByFloWId(){
        List<RocketMessage> messages = new ArrayList<>();
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        List<MqProducerMsgPo> productMsgPos = daoSupport.selectList(productMsgPo);
        if (productMsgPos != null && productMsgPos.size() > 0) {
            for (int i = 0; i < productMsgPos.size(); i++) {
                MqProducerMsgPo productMsgPo1=productMsgPos.get(i);
                String messageJson = productMsgPo1.getMessage();
                RocketMessage message = gson.fromJson(messageJson,
                        new TypeToken<RocketMessage>() {
                        }.getType());
                messages.add(message);
            }
        }
        return messages;
    }

    /**
     * @return int
     * @Author guihj
     * @Description 更新消息状态为2  准备发送状态
     * @Date 2019/4/11 22:38
     * @Param [status]
     **/
    @Override
    public int updateMsgStatusSend() {
        //set
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setLastUpdate(DateUtil.getCurrentDate());
        productMsgPo.setStatus(MessageStatus.STATUS_SEND);
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        return daoSupport.update(productMsgPo);
    }

    /**
     * @return int
     * @Author guihj
     * @Description 更新消息状态为4   发送异常状态
     * @Date 2019/4/11 22:38
     * @Param [status]
     **/
    @Override
    public int updateMsgStatusException() {
        //set
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setLastUpdate(DateUtil.getCurrentDate());
        productMsgPo.setStatus(MessageStatus.STATUS_EXCEPTION);
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        return daoSupport.update(productMsgPo);
    }


    /**
     * @return int
     * @Author guihj
     * @Description  定时任务 更新消息状态为4   发送异常状态
     * @Date 2019/4/11 22:38
     * @Param [status]
     **/
    public int scheduledUpdateMsgException(String messageId) {
        //set
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setLastUpdate(DateUtil.getCurrentDate());
        productMsgPo.setStatus(MessageStatus.STATUS_EXCEPTION);
        productMsgPo.setMqMsgId(messageId);
        return daoSupport.update(productMsgPo);
    }


    /**
     * @return int
     * @Author guihj
     * @Description 更新状态为3, 表示消息发送成功,记录消息详细信息
     * @Date 2019/4/11 22:38
     * @Param [status]
     **/
    @Override
    public int updateMessageSuccess(RocketMessage message,SendResult sendResult){
        //set
        MqProducerMsgPo productMsgPo = new MqProducerMsgPo();
        productMsgPo.setLastUpdate(DateUtil.getCurrentDate());
        productMsgPo.setStatus(MessageStatus.STATUS_SUCCESS);
        productMsgPo.setMqMsgId(message.getMessageId());
        productMsgPo.setBrokerName(sendResult.getMessageQueue().getBrokerName());
        productMsgPo.setOffsetMsgId(sendResult.getOffsetMsgId());
        productMsgPo.setMsgId(sendResult.getMsgId());
        productMsgPo.setQueueId(sendResult.getMessageQueue().getQueueId());
        return daoSupport.update(productMsgPo);
    }


}
