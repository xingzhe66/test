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
public class AppMain implements CommandLineRunner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class);
    @Autowired
    MsgProducerImple mqService;
    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        mqService.sendMessage("GUIHJ","JAVA","这是一条消息");
    }
}
