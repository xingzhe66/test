package com.dcits.comet.flow;

import com.dcits.comet.commons.Context;
import com.dcits.comet.commons.ThreadLocalManager;
import com.dcits.comet.flow.model.MessageIn;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName FolwApplicationTest
 * @Author guihj
 * @Date 2019/4/11 9:50
 * @Description TODO
 * @Version 1.0
 **/
@SpringBootApplication
@EnableScheduling
//@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.dcits.comet"})
@Slf4j
public class FolwApplicationTest implements CommandLineRunner {


    @Autowired
    private ExecutorFlow executorFlow;
    public static void main(String[] args) {
        SpringApplication.run(FolwApplicationTest.class, args);
    }

    @Override
    public void run(String... args){
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
        log.info("flows  start--------------");
        MessageIn messageIn=new MessageIn();
        messageIn.setMessageId("345465");
        executorFlow.start("messageFlow",messageIn);
    }
}
