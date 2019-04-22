package com.dcits.comet.flow.bf;

import com.dcits.comet.flow.AbstractFlow;
import com.dcits.comet.flow.model.MessageIn;
import com.dcits.comet.flow.model.MessageOut;
import com.dcits.comet.mq.api.IMsgProducer;
import com.dcits.comet.mq.api.RocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessageFlow
 * @Author guihj
 * @Date 2019/4/11 15:05
 * @Description TODO
 * @Version 1.0
 **/
@Component
@Slf4j
public class MessageFlow  extends AbstractFlow<MessageIn, MessageOut> {
    @Autowired
    IMsgProducer iMsgProducer;

    @Override
    public MessageOut execute(MessageIn input) {

        MessageOut messageOut=new MessageOut();
        log.info("执行业务逻辑-------");

        //log.info("消息队列----1");

        //iMsgProducer.sendMessage("string message");
        log.info("消息队列----1");
        iMsgProducer.sendMessage("dcits","dc","这是第一条消息");
        RocketMessage message=new RocketMessage();
        message.setMsgText("message type这是第二条消息");
        message.setTag("guihj");
        message.setTopic("guihj");
        iMsgProducer.sendMessage(message);
        messageOut.setMessageTxt(message.getMsgText());
        return messageOut;
    }
}
