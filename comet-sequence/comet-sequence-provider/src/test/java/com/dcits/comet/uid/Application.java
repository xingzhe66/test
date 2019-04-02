package com.dcits.comet.uid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/1 17:26
 **/
@SpringBootApplication(scanBasePackages = {"com.dcits.comet"})
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }




}
