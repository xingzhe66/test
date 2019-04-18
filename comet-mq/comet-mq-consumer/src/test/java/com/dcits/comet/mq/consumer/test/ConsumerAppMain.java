package com.dcits.comet.mq.consumer.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author wangyun
 * @Date 2019/4/18
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.dcits.comet"})
@EnableConfigurationProperties
public class ConsumerAppMain implements CommandLineRunner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ConsumerAppMain.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerAppMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
