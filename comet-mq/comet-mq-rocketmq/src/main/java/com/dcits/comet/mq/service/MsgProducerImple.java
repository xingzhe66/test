package com.dcits.comet.mq.service;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.mq.api.IMsgProducer;
import com.dcits.comet.mq.api.RocketMessage;
import com.dcits.comet.mq.model.ProductMsgPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @ClassName MQService
 * @Author guihj
 * @Date 2019/4/10 18:40
 * @Description TODO
 * @Version 1.0
 **/
@Component
public class MsgProducerImple implements IMsgProducer {

   @Autowired
   MessageService messageService;

    @Override
    public void sendMessage(String topic, String tags, String messageTxt) {
        ProductMsgPo productMsgPo=messageService.messageConvert();
        RocketMessage message=new RocketMessage();
        message.setMsgText(messageTxt);
        message.setMsgText(topic);
        message.setTag(tags);
        productMsgPo.setMessage(JSON.toJSONString(message));
        messageService.saveMessage(productMsgPo);
    }

    @Override
    public void sendMessage(String topic, String messageTxt) {
        ProductMsgPo productMsgPo=messageService.messageConvert();
        RocketMessage message=new RocketMessage();
        message.setMsgText(messageTxt);
        message.setTopic(topic);
        productMsgPo.setMessage(JSON.toJSONString(message));
        messageService.saveMessage(productMsgPo);
    }

    @Override
    public void sendMessage(RocketMessage message) {
        ProductMsgPo productMsgPo=messageService.messageConvert();
        productMsgPo.setMessage(JSON.toJSONString(message));
        messageService.saveMessage(productMsgPo);
    }
}
