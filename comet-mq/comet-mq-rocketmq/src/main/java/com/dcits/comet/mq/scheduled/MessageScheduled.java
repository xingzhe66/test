package com.dcits.comet.mq.scheduled;

import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.mq.api.RocketMessage;
import com.dcits.comet.mq.model.ProductMsgPo;
import com.dcits.comet.mq.service.MessageService;
import com.dcits.comet.mq.service.MsgServiceImple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MessageScheduled {
    @Autowired
    MsgServiceImple msgServiceImple;

    @Autowired
    MessageService messageService;

    @Autowired
    Gson gson;
    /**
     * @Author guihj
     * @Description 定时扫描状态为1的消息，如果时间与当前时间超过五分钟则将状态更新为4，
     * 说明流程异常，消息无法发送，需要人工干预
     * @Date 2019/4/14 20:54
     * @Param []
     * @return void
     **/
//    @Scheduled(cron = "0/10 * * * * ?")
    public void updateMqStatusCron() throws InterruptedException {
        log.info("定时扫描状态为1的消息");
        List<ProductMsgPo> productMsgPos=messageService.getMsgStatusOne();
        if(productMsgPos !=null && productMsgPos.size()>0){
            for(int i=0;i<productMsgPos.size();i++){
                ProductMsgPo  productMsgPo= productMsgPos.get(i);
                String createTime =productMsgPo.getCreateTime();
                long timeDifference= DateUtil.getTimeDifference(createTime);
                int mmTime=(int)timeDifference/(1000*60);
                if(mmTime>5){
                    msgServiceImple.updateMsgStatusException();
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
//    @Scheduled(cron = "0/5 * * * * ?")
    public void autoSendMqCron() throws Exception {
        log.info("定时扫描状态为2的消息");
        List<ProductMsgPo> productMsgPos=messageService.getMsgStatusTwo();
        if(productMsgPos !=null && productMsgPos.size()>0){
            for(int i=0;i<productMsgPos.size();i++){
                ProductMsgPo  productMsgPo= productMsgPos.get(i);
                int msgStatus=productMsgPo.getStatus();
                if(msgStatus==2){
                    RocketMessage  rocketMessage= gson.fromJson(productMsgPo.getMessage(),
                            new TypeToken<RocketMessage>() {
                            }.getType());
                    rocketMessage.setMessageId(productMsgPo.getProductedMsgId());
                    msgServiceImple.send(rocketMessage);
                }
            }
        }
    }

}
