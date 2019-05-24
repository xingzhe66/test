package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.Segment;
import com.dcits.comet.batch.annotation.BatchConfiguration;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.batch.sonic.annotation.EnableSonicExecutorServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 10:14
 **/
@SpringBootApplication(scanBasePackages = "com.dcits.comet", exclude = {DataSourceAutoConfiguration.class})
@BatchConfiguration
@EnableTransactionManagement
@EnableSonicExecutorServer
@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class);

       /* SonicExecutorServer sonicExecutorServer = SoincSpringApplicationContext.getContext().getBean(SonicExecutorServer.class);

        System.out.println(sonicExecutorServer.getLockFactory().getLock("demo"));*/

    }

    @Configuration
    class applicationApp {
        @Autowired
        BatchStep batchStep;

        @Bean
        public String doDemo() {
            BatchContext batchContext = new BatchContext();
            List<String> list = batchStep.getNodeList(batchContext);
            log.info("list{}",list);
            for (String node : list) {
                List<Segment> segments = batchStep.getSegmentList(batchContext, node);
                log.info("segments{}",segments);
            }
            return "";
        }
    }

}

