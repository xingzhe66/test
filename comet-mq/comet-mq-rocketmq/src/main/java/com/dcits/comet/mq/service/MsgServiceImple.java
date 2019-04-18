package com.dcits.comet.mq.service;

import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.mq.api.IMsgService;
import com.dcits.comet.mq.api.RocketMessage;
import com.dcits.comet.mq.contanst.MessageStatus;
import com.dcits.comet.mq.model.ProductMsgPo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public  String  send(RocketMessage rocketMessage) throws Exception {
        Message sendMsg = new Message(
                rocketMessage.getTopic(), rocketMessage.getTag(),
                rocketMessage.getMsgText().getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        return sendResult.getSendStatus().toString();
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
        ProductMsgPo productMsgPo = new ProductMsgPo();
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        List<ProductMsgPo> productMsgPos = daoSupport.selectList(productMsgPo);
        if (productMsgPos != null && productMsgPos.size() > 0) {
            for (int i = 0; i < productMsgPos.size(); i++) {
                ProductMsgPo productMsgPo1=productMsgPos.get(i);
                String messageJson = productMsgPo1.getMessage();
                RocketMessage message = gson.fromJson(messageJson,
                        new TypeToken<RocketMessage>() {
                        }.getType());
                message.setMessageId(productMsgPo1.getProductedMsgId());
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
        ProductMsgPo productMsgPo = new ProductMsgPo();
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
        ProductMsgPo productMsgPo = new ProductMsgPo();
        productMsgPo.setLastUpdate(DateUtil.getCurrentDate());
        productMsgPo.setStatus(MessageStatus.STATUS_EXCEPTION);
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        return daoSupport.update(productMsgPo);
    }

    /**
     * @return int
     * @Author guihj
     * @Description 更新状态为3, 表示消息发送成功
     * @Date 2019/4/11 22:38
     * @Param [status]
     **/
    @Override
    public int updateMessageSuccess(RocketMessage message){
        //set
        ProductMsgPo productMsgPo = new ProductMsgPo();
        productMsgPo.setLastUpdate(DateUtil.getCurrentDate());
        productMsgPo.setStatus(MessageStatus.STATUS_SUCCESS);
        productMsgPo.setFlowId(Context.getInstance().getFlowId());
        productMsgPo.setProductedMsgId(message.getMessageId());
        return daoSupport.update(productMsgPo);
    }


}
