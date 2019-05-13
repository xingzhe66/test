package com.dcits.comet.batch.sonic;

import com.dcits.comet.batch.annotation.BatchConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/7 10:14
 **/
@SpringBootApplication
@BatchConfiguration
@EnableSonicExecutorServer
public class ServerMain {
    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class);

    }
}

