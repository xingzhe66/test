package com.dcits.comet.mq.producer.rocketmq.scheduled;

import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.mq.api.RocketMessage;
import com.dcits.comet.mq.producer.rocketmq.service.MessageService;
import com.dcits.comet.mq.producer.rocketmq.service.MsgServiceImple;
import com.dcits.comet.mq.producer.rocketmq.model.MqProducerMsgPo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @ClassName MessageScheduled
 * @Author guihj
 * @Date 2019/4/14 20:52
 * @Description 消息定时处理类
 * @Version 1.0
 **/
@Component
@Slf4j
@Configuration
@ComponentScan({"com.dcits.comet.mq.producer.rocketmq.service"})
public class MessageScheduled {
    @Autowired
    MsgServiceImple msgServiceImple;

    @Autowired
    MessageService messageService;

    @Autowired
    Gson gson;


    @Value("${rocketmq.scheduled.exceptionTimeout}")
    private int exceptionTimeout;

    /**
     * @Author guihj
     * @Description 定时扫描状态为1的消息，如果时间与当前时间超过五分钟则将状态更新为4，
     * 说明流程异常，消息无法发送，需要人工干预
     * @Date 2019/4/14 20:54
     * @Param []
     * @return void
     **/
    // @Scheduled(cron = "0/10 * * * * ?")
    @Scheduled(cron = "${rocketmq.scheduled.corn1}")
    public void updateMqStatusCron() throws InterruptedException {
        log.info("定时扫描状态为1的消息");
        List<MqProducerMsgPo> productMsgPos=messageService.getMsgStatusOne();
        if(productMsgPos !=null && productMsgPos.size()>0){
            for(int i=0;i<productMsgPos.size();i++){
                MqProducerMsgPo  productMsgPo= productMsgPos.get(i);
                String createTime =productMsgPo.getCreateTime();
                long timeDifference= DateUtil.getTimeDifference(createTime);
                int mmTime=(int)timeDifference/(1000*60);
                if(mmTime>(exceptionTimeout/60)){
                    msgServiceImple.scheduledUpdateMsgException(productMsgPo.getMqMsgId());
                }
            }
        }
    }

    /**
     * @Author guihj
     * @Description 定时扫描状态为2的消息，然后自动发送
     * @Date 2019/4/14 20:56
     * @Param []
     * @return void
     **/
 // @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "${rocketmq.scheduled.corn2}")
    public void autoSendMqCron() throws Exception {
        log.info("定时扫描状态为2的消息");
        List<MqProducerMsgPo> productMsgPos=messageService.getMsgStatusTwo();
        if(productMsgPos !=null && productMsgPos.size()>0){
            for(int i=0;i<productMsgPos.size();i++){
                MqProducerMsgPo  productMsgPo= productMsgPos.get(i);
                int msgStatus=productMsgPo.getStatus();
                if(msgStatus==2){
                    RocketMessage  rocketMessage= gson.fromJson(productMsgPo.getMessage(),
                            new TypeToken<RocketMessage>() {
                            }.getType());
                    SendResult sendResult= msgServiceImple.realSend(rocketMessage);
                    if("SEND_OK".equals(sendResult.getSendStatus().toString())){
                        msgServiceImple.updateMessageSuccess(rocketMessage,sendResult);
                    }
                }
            }
        }
    }
}
