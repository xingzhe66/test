package com.dcits.comet.mq.rocketmq;


import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.ThreadLocalManager;
import com.dcits.comet.commons.utils.DateUtil;
import com.dcits.comet.commons.utils.StringUtil;
import com.dcits.comet.mq.api.IMsgService;
import com.dcits.comet.mq.api.RocketMessage;
import com.dcits.comet.mq.rocketmq.service.MsgProducerImple;
import com.dcits.comet.mq.rocketmq.service.MsgServiceImple;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;


@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.dcits.comet"})
@Slf4j
public class RocketMqTestMain implements CommandLineRunner {

    @Autowired
    MsgProducerImple mqService;
    @Autowired
    IMsgService iMsgService;
    public static void main(String[] args) {
        SpringApplication.run(RocketMqTestMain.class, args);
    }
    @Override
    public void run(String... args) throws Exception {

        log.info("init context-----------------");
        Context context = Context.builder()
                .platformId(ThreadLocalManager.getUUID())
                .reference(ThreadLocalManager.getUUID())
//                .tranDate(sysHead.getTranDate())
//                .userId(sysHead.getUserId())
//                .userLang(sysHead.getUserLang())
//                .tranBranch(sysHead.getBranchId())
//                .sourceType(sysHead.getSourceType())
//                .seqNo(sysHead.getSeqNo())
//                .programId(sysHead.getProgramId())
//                .company(sysHead.getCompany())
                .isBatch(false)
//                .sysHead(sysHead)
//                .appHead(appHead)
                .sysHead(null)
                .appHead(null)
                .build();

        context.init(context);
        context.getInstance().setFlowId(StringUtil.getUUID());
        for(int i=0;i<20;i++){
            mqService.sendMessage("dcits","JAVA","消息顺序"+i);
        }

        iMsgService.updateMsgStatusSend();
        //获取对应flowId的消息集合
        List<RocketMessage> rocketMessages = iMsgService.getMessageByFloWId();
        if (rocketMessages != null && rocketMessages.size() > 0) {
            for (int i = 0; i < rocketMessages.size(); ++i) {
                RocketMessage rocketMessage = (RocketMessage) rocketMessages.get(i);
                //调用rocketMq真实发送消息
                SendResult sendResult = iMsgService.realSend(rocketMessage);
                if ("SEND_OK".equals(sendResult.getSendStatus().toString())) {
                    log.info("mq message send success-----------------");
                    //更新消息状态  3
                    iMsgService.updateMessageSuccess(rocketMessage,sendResult);
                }
            }
        }
    }
}
