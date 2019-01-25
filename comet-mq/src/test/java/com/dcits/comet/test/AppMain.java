package com.dcits.comet.test;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = WebMvcConfigurer.class)
@ComponentScan(basePackages = { "com.dcits"})
public class AppMain implements CommandLineRunner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class);
    @Autowired
    private ApplicationContext applicationContext;
    /**使用RocketMq的生产者*/
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public static void main(String[] args) {

        new SpringApplicationBuilder(AppMain.class)
                .web(WebApplicationType.NONE)
                .run(args);


//        SpringApplication app =new SpringApplication(Appmain.class);
//        app.setWebApplicationType(WebApplicationType.NONE);
//        app.run(args);

    }

    @Override
    public void run(String... args) throws Exception {
        //DaoSupport daoSupport = (DaoSupport) applicationContext.getBean("daoSupport");
        String msg = "demo msg test";
        LOGGER.info("开始发送消息："+msg);
        Message sendMsg = new Message("DemoTopic","DemoTag",msg.getBytes());
        //默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        LOGGER.info("消息发送响应信息："+sendResult.toString());
    }
}
