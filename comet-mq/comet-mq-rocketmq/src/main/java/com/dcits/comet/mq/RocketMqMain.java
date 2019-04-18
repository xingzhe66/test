package com.dcits.comet.mq;

import com.dcits.comet.mq.service.MsgProducerImple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dcits.comet","com.dcits.comet.mq"})
public class RocketMqMain implements CommandLineRunner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RocketMqMain.class);
    public static void main(String[] args) {
        SpringApplication.run(RocketMqMain.class, args);
    }

    @Override
    public void run(String... args){

    }
}
