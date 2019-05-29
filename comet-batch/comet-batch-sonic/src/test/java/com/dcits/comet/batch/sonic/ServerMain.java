package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.annotation.BatchConfiguration;
import com.dcits.comet.batch.sonic.annotation.EnableSonicExecutorServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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


}

