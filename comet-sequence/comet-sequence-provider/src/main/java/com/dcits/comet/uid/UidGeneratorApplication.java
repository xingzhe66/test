package com.dcits.comet.uid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author leijian
 * @date 2019年4月5日
 */
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class UidGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(UidGeneratorApplication.class, args);
    }
}

