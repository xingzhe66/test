package com.dcits.comet.uid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author leijian
 * @date 2019年4月5日
 */
@SpringBootApplication
@EnableEurekaClient
public class UidGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(UidGeneratorApplication.class, args);
    }
}

