package com.dcits.comet.flow.service;

import com.dcits.comet.flow.constant.MsgSendStatus;
import com.dcits.comet.mq.api.IMsgService;
import com.dcits.comet.mq.api.RocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName MqService
 * @Author guihj
 * @Date 2019/4/30 17:25
 * @Description TODO
 * @Version 1.0
 **/
@Slf4j
@Component
@ConditionalOnProperty(name = "rocketmq.isEnable", havingValue = "true", matchIfMissing = false)
public class MqService {
    @Autowired
    IMsgService iMsgService;

    /**
     * @return void
     * @Author guihj
     * @Description 更新消息发送状态 2  并真实发送消息
     * @Date 2019/4/29 15:26
     * @Param [out]
     **/
    public void mqHandler() {
        try {
            try {
                //更新消息发送状态  2
                iMsgService.updateMsgStatusSend();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            //获取对应flowId的消息集合
            List<RocketMessage> rocketMessages = iMsgService.getMessageByFloWId();
            if (rocketMessages != null && rocketMessages.size() > 0) {
                for (int i = 0; i < rocketMessages.size(); ++i) {
                    RocketMessage rocketMessage = (RocketMessage) rocketMessages.get(i);
                    //调用rocketMq真实发送消息
                    SendResult sendResult = iMsgService.realSend(rocketMessage);
                    if (MsgSendStatus.SEND_OK.toString().equals(sendResult.getSendStatus().toString())) {
                        log.info("mq message send success-----------------");
                        //更新消息状态  3
                        iMsgService.updateMessageSuccess(rocketMessage, sendResult);
                    }
                }
            }
        } catch (Exception var7) {
            log.info("mq message send fail------------------");
            var7.printStackTrace();
        }
    }

    /**
     * @return void
     * @Author guihj
     * @Description 流程执行异常，修改消息状态
     * @Date 2019/4/29 15:24
     * @Param [e]
     **/
    public void updateExceptionStatus() {
        try {
            iMsgService.updateMsgStatusException();
        } catch (Exception e1) {
            log.info("update  Status  Exception  fail------------------");
            e1.printStackTrace();
        }
    }
}
